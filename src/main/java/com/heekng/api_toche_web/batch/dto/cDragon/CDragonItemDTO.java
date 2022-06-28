package com.heekng.api_toche_web.batch.dto.cDragon;

import com.heekng.api_toche_web.util.Fnv1aHash;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class CDragonItemDTO {
    private String desc;
    private Map<String, Float> effects;
    private List<Integer> from;
    private String icon;
    private Integer id;
    private String name;
    private Boolean unique;

    private String nameEn;

    public void fetchDesc() {
        updateDescFromEffects();
        deleteKeywordFromDesc();
    }

    private void deleteKeywordFromDesc() {
        List<String> deleteKeywords = List.of("scaleAP", "scaleRange", "scaleAS", "scaleMana", "scaleAD", "scaleHealth", "scaleCritMult", "scaleCrit", "goldCoins", "scaleArmor", "star", "scaleMR");
        for (String keyword : deleteKeywords) {
            this.desc = this.desc.replace(" %i:"+keyword+"%", "");
        }
    }

    private void updateDescFromEffects() {
        List<String> effectKeywords = getEffectKeywordFromDesc();
        List<String> filterMultipleByEffectKeywords = filterMultipleByEffectKeywords(effectKeywords);
        Map<String, String> hashEffectMap = Fnv1aHash.getHashTextAndTextMapByTexts(filterMultipleByEffectKeywords);
        Map<String, Float> convertEffectsByHashEffectMap = getConvertEffectsByHashEffectMap(hashEffectMap);
        for (String effectKeyword : effectKeywords) {
            String mapKey = effectKeyword;
            Float mapValue = convertEffectsByHashEffectMap.get(mapKey);
            int multipleIndex = effectKeyword.indexOf("*100");
            if (multipleIndex != -1) {
                mapKey = effectKeyword.substring(0, multipleIndex);
                mapValue = convertEffectsByHashEffectMap.get(mapKey) * 100;
            }
            this.desc = this.desc.replace("@" + effectKeyword + "@", String.valueOf(mapValue));
        }
    }

    private Map<String, Float> getConvertEffectsByHashEffectMap(Map<String, String> hashEffectMap) {
        Map<String, Float> decodeEffectsMap = new HashMap<>();
        for (String effectsKey : this.effects.keySet()) {
            Float effectValue = this.effects.get(effectsKey);
            decodeEffectsMap.put(hashEffectMap.getOrDefault(effectsKey, effectsKey), effectValue);
        }
        return decodeEffectsMap;
    }

    private List<String> getEffectKeywordFromDesc() {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(.*?)@");
        Matcher matcher = pattern.matcher(this.desc);
        while (matcher.find()) {
            results.add(matcher.group(1));
        }
        return results;
    }

    private List<String> filterMultipleByEffectKeywords(List<String> effectKeywords) {
        return effectKeywords.stream()
                .map(effectKeyword -> {
                    int multipleIndex = effectKeyword.indexOf("*100");
                    if (multipleIndex != -1) {
                        effectKeyword = effectKeyword.substring(0, multipleIndex);
                    }
                    return effectKeyword;
                })
                .collect(Collectors.toList());
    }

}
