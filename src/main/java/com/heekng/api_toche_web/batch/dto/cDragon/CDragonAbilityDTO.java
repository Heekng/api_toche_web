package com.heekng.api_toche_web.batch.dto.cDragon;

import com.heekng.api_toche_web.entity.Ability;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class CDragonAbilityDTO {

    private String desc;
    private String icon;
    private String name;
    private List<CDragonChampionAbilityVariableDTO> variables;

    private String enName;

    public Ability toAbilityEntity(String CDRAGON_PATH_IMAGE) {
        return Ability.builder()
                .iconPath(CDRAGON_PATH_IMAGE + this.icon.toLowerCase().replace(".dds", ".png"))
                .abilityDesc(desc)
                .krName(name)
                .name(enName.equals("") ? null : enName)
                .build();
    }

    public String getNullSafeName() {
        return StringUtils.hasText(name) ? name : "";
    }

    public void updateDescToUsageDesc() {
        if (desc != null) {
            deleteIconNameFromDesc();
            updateDescFromVariables();
        }
    }

    private void updateDescFromVariables() {
        if (variables != null && !variables.isEmpty()) {
            Map<String, List<Float>> variablesMap = getStringAndFloatListMapFromVariables();
            List<String> variableKeywords = getVariableKeywordFromDesc();
            Map<String, String> variableValueStringMap = getVariableValueStringMapByVariablesMapAndVariableKeywords(variablesMap, variableKeywords);
            updateDescByVariableValueStringMap(variableValueStringMap);
        }
    }

    private void updateDescByVariableValueStringMap(Map<String, String> variableValueStringMap) {
        for (String variableString : variableValueStringMap.keySet()) {
            String variableValue = variableValueStringMap.get(variableString);
            this.desc = this.desc.replace(variableString, variableValue);
        }
    }

    private void deleteIconNameFromDesc() {
        List<String> iconNameKeywords = List.of("scaleAP", "scaleRange", "scaleAS", "scaleMana", "scaleAD", "scaleHealth", "scaleCritMult", "scaleCrit", "goldCoins", "scaleArmor", "star", "scaleMR");
        for (String iconNameKeyword : iconNameKeywords) {
            this.desc = this.desc.replace(" %i:"+iconNameKeyword+"%", "");
        }
    }

    private Map<String, String> getVariableValueStringMapByVariablesMapAndVariableKeywords(Map<String, List<Float>> variablesMap, List<String> variableKeywords) {
        Map<String, String> replaceMap = new HashMap<>();
        for (String variableKeyword : variableKeywords) {
            String deleteModifiedKeyword = variableKeyword
                    .replace("Modified", "")
                    .replace("Tooltip", "")
                    .replace("%", "");
            Integer multipleValue = 1;
            String realKeyword = deleteModifiedKeyword;
            int multipleIndex = deleteModifiedKeyword.indexOf("*");
            if (multipleIndex != -1) {
                multipleValue *= Integer.parseInt(deleteModifiedKeyword.substring(multipleIndex + 1));
                realKeyword = deleteModifiedKeyword.substring(0, multipleIndex);
            }
            if (variablesMap.containsKey(realKeyword)) {
                List<Float> variableValues = variablesMap.get(realKeyword);
                Float variableValueLevel1 = variableValues.get(1) * multipleValue;
                Float variableValueLevel2 = variableValues.get(2) * multipleValue;
                Float variableValueLevel3 = variableValues.get(3) * multipleValue;
                String variableValueString = variableValueLevel1 + "/" + variableValueLevel2 + "/" + variableValueLevel3;
                if (variableValueLevel1.equals(variableValueLevel2) && variableValueLevel2.equals(variableValueLevel3)) {
                    variableValueString = String.valueOf(variableValueLevel1);
                }
                replaceMap.put("@" + variableKeyword + "@", variableValueString);
            } else {
                replaceMap.put(">@" + variableKeyword + "@<", "><");
                replaceMap.put("@" + variableKeyword + "@ ", "");
                replaceMap.put("@" + variableKeyword + "@의 ", "");
                replaceMap.put(">의 ", ">");
            }
        }
        return replaceMap;
    }

    private Map<String, List<Float>> getStringAndFloatListMapFromVariables() {
        return this.variables.stream()
                .filter(variables -> variables.getValue() != null)
                .collect(Collectors.toMap(CDragonChampionAbilityVariableDTO::getName,CDragonChampionAbilityVariableDTO::getValue));
    }

    private List<String> getVariableKeywordFromDesc() {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(.*?)@");
        Matcher matcher = pattern.matcher(this.desc);
        while (matcher.find()) {
            results.add(matcher.group(1));
        }
        return results;
    }
}
