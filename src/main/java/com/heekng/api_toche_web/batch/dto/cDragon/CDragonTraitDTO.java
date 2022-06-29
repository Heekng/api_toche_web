package com.heekng.api_toche_web.batch.dto.cDragon;

import com.heekng.api_toche_web.util.Fnv1aHash;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class CDragonTraitDTO {

    private String apiName;
    private String desc;
    private List<CDragonTraitEffectDTO> effects;
    private String icon;
    private String name;

    public void updateDesc() {
        if (desc != null) {
            updateDescFromEffects();
        }
    }

    private void updateDescFromEffects() {
        if (effects != null && !effects.isEmpty()) {
            updateEffectsVariablesToHash();
            this.desc = getConvertedDesc();
        }
    }

    private void updateEffectsVariablesToHash() {
        this.effects.forEach(CDragonTraitEffectDTO::updateVariablesToHash);
    }

    private String getDefaultDesc() {
        int expandRowTagIndex = this.desc.indexOf("<expandRow>");
        int rowTagIndex = this.desc.indexOf("<row>");
        String defaultDesc = this.desc;
        if (expandRowTagIndex != -1) {
            defaultDesc = this.desc.substring(0, expandRowTagIndex);
        } else if (rowTagIndex != -1) {
            defaultDesc = this.desc.substring(0, rowTagIndex);
        }
        return defaultDesc;
    }

    private String getExpandRowDesc() {
        int expandRowTagIndex = this.desc.indexOf("<expandRow>");
        return this.desc.substring(expandRowTagIndex);
    }

    private List<String> getRowDescList() {
        List<String> rowDescList = new ArrayList<>();
        List<Integer> rowTagIndexList = new ArrayList<>();
        Integer beforeIndex = 0;
        while (true) {
            Integer index = this.desc.indexOf("<expandRow>", beforeIndex + 11);
            if (index == -1) {
                break;
            }
            rowTagIndexList.add(index);
        }
        for (int i = 0; i < rowTagIndexList.size(); i++) {
            Integer startIndex = rowTagIndexList.get(i);
            Integer endIndex;
            if (rowTagIndexList.size() == i + 1) {
                rowDescList.add(this.desc.substring(startIndex));
            } else {
                endIndex = rowTagIndexList.get(i + 1);
                rowDescList.add(this.desc.substring(startIndex, endIndex));
            }
        }
        return rowDescList;
    }

    private String getConvertedDesc() {
        String defaultDesc = getConvertedDefaultDesc();
        String expandRowDesc = "";
        String rowDesc = "";
        if (this.desc.contains("<expandRow>")) {
            expandRowDesc = getConvertedExpandRowDesc();
        }
        if (this.desc.contains("<row>")) {
            rowDesc = getConvertedRowDesc();
        }
        return defaultDesc + expandRowDesc + rowDesc;
    }

    private String getConvertedDefaultDesc() {
        String defaultDesc = getDefaultDesc();
        List<String> effectKeywords = getEffectKeywordByTextWithoutMinUnits(defaultDesc);
        Map<String, String> hashEffectKeywordMap = getEffectKeywordAndHashKeywordMapByEffectKeywords(effectKeywords);
        return getDescFromEffectsByDescAndHashEffectKeywordMap(defaultDesc, hashEffectKeywordMap);
    }

    private String getConvertedRowDesc() {
        List<String> rowDescList = getRowDescList();
        StringBuilder resultExpandRowDescBuilder = new StringBuilder();

        for (int i = 0; i < rowDescList.size(); i++) {
            String rowDesc = rowDescList.get(i);
            List<String> effectKeywords = getEffectKeywordByTextWithoutMinUnits(rowDesc);
            Map<String, String> hashEffectKeywordMap = getEffectKeywordAndHashKeywordMapByEffectKeywords(effectKeywords);
            CDragonTraitEffectDTO effectDto = this.effects.get(i);

            String copyExpandRowDesc = rowDesc;

            copyExpandRowDesc = copyExpandRowDesc.replace("@MinUnits@", String.valueOf(effectDto.getMinUnits()));
            for (String effectKeyword : hashEffectKeywordMap.keySet()) {
                String hashEffectKeyword = hashEffectKeywordMap.get(effectKeyword);
                int multipleIndex = effectKeyword.indexOf("*");
                int multipleValue = 1;
                if (multipleIndex != -1) {
                    multipleValue *= Integer.parseInt(effectKeyword.substring(multipleIndex + 1));
                }
                Float effectValue = effectDto.getVariables().get(hashEffectKeyword) * multipleValue;
                copyExpandRowDesc = copyExpandRowDesc.replace("@" + effectKeyword + "@", String.valueOf(effectValue));
            }
            resultExpandRowDescBuilder.append(copyExpandRowDesc);
        }
        return resultExpandRowDescBuilder.toString();
    }

    private String getConvertedExpandRowDesc() {
        String expandRowDesc = getExpandRowDesc();
        List<String> effectKeywords = getEffectKeywordByTextWithoutMinUnits(expandRowDesc);
        Map<String, String> hashEffectKeywordMap = getEffectKeywordAndHashKeywordMapByEffectKeywords(effectKeywords);

        StringBuilder resultExpandRowDescBuilder = new StringBuilder();
        for (CDragonTraitEffectDTO effectDto : this.effects) {
            String copyExpandRowDesc = expandRowDesc;
            copyExpandRowDesc = copyExpandRowDesc.replace("@MinUnits@", String.valueOf(effectDto.getMinUnits()));
            for (String effectKeyword : hashEffectKeywordMap.keySet()) {
                String hashEffectKeyword = hashEffectKeywordMap.get(effectKeyword);
                int multipleIndex = effectKeyword.indexOf("*");
                int multipleValue = 1;
                if (multipleIndex != -1) {
                    multipleValue *= Integer.parseInt(effectKeyword.substring(multipleIndex + 1));
                }
                Float effectValue = effectDto.getVariables().get(hashEffectKeyword) * multipleValue;
                copyExpandRowDesc = copyExpandRowDesc.replace("@" + effectKeyword + "@", String.valueOf(effectValue));
            }
            resultExpandRowDescBuilder.append(copyExpandRowDesc)
                    .append("<br>");
        }
        return resultExpandRowDescBuilder.toString();
    }

    private String getDescFromEffectsByDescAndHashEffectKeywordMap(String desc, Map<String, String> hashEffectKeywordMap) {
        String resultDesc = desc;
        resultDesc = resultDesc.replace("@MinUnits@", String.valueOf(this.effects.get(0).getMinUnits()));
        for (String effectKeyword : hashEffectKeywordMap.keySet()) {
            StringBuilder changeStringBuilder = new StringBuilder();
            String hashEffectKeyword = hashEffectKeywordMap.get(effectKeyword);
            Iterator<CDragonTraitEffectDTO> effectDTOIterator = this.effects.iterator();
            while (effectDTOIterator.hasNext()) {
                CDragonTraitEffectDTO effectDto = effectDTOIterator.next();
                Float effectValue = effectDto.getVariables().get(hashEffectKeyword);
                changeStringBuilder.append(effectValue);

                if (effectDTOIterator.hasNext()) {
                    changeStringBuilder.append("/");
                }
            }
            resultDesc = resultDesc.replace("@" + effectKeyword + "@", changeStringBuilder.toString());
        }
        return resultDesc;
    }

    private List<String> getEffectKeywordByTextWithoutMinUnits(String text) {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(.*?)@");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String keyword = matcher.group(1);
            if (!keyword.equals("MinUnits")) {
                results.add(keyword);
            }
        }
        return results;
    }

    private Map<String, String> getEffectKeywordAndHashKeywordMapByEffectKeywords(List<String> effectKeywords) {
        return effectKeywords.stream()
                .collect(Collectors.toMap(
                        effectKeyword -> effectKeyword,
                        effectKeyword -> {
                            int multipleIndex = effectKeyword.indexOf("*");
                            String realEffectKeyword = effectKeyword;
                            if (multipleIndex != -1) {
                                realEffectKeyword = effectKeyword.substring(0, multipleIndex);
                            }
                            return Fnv1aHash.hashCDragonValue(realEffectKeyword);
                        }
                ));
    }
}
