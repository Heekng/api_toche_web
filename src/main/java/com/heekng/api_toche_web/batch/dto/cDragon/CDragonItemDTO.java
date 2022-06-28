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
    private Map<String, Float> hashEffects = new HashMap<>();

    public void fetchDesc() {
        updateEffectsKeysFromHash();
        deleteIconNameFromDesc();
        updateDescFromEffects();
    }

    private void updateEffectsKeysFromHash() {
        for (String effectsKey : this.effects.keySet()) {
            String hashEffectsKey = effectsKey;
            Float effectsValue = this.effects.get(effectsKey);
            if (!effectsKey.contains("{")) {
                hashEffectsKey = Fnv1aHash.hashCDragonValue(hashEffectsKey);
            }
            hashEffects.put(hashEffectsKey, effectsValue);
        }
    }

    private void deleteIconNameFromDesc() {
        List<String> iconNameKeywords = List.of("scaleAP", "scaleRange", "scaleAS", "scaleMana", "scaleAD", "scaleHealth", "scaleCritMult", "scaleCrit", "goldCoins", "scaleArmor", "star", "scaleMR");
        for (String iconNameKeyword : iconNameKeywords) {
            this.desc = this.desc.replace(" %i:"+iconNameKeyword+"%", "");
        }
    }

    private void updateDescFromEffects() {
        List<String> effectKeywords = getEffectKeywordFromDesc();
        List<String> deleteMultipleByEffectKeywords = getDeleteMultipleByEffectKeywords(effectKeywords);
        Map<String, String> hashEffectMap = Fnv1aHash.getTextAndHashTextMapByTexts(deleteMultipleByEffectKeywords);

        for (String effectKeyword : effectKeywords) {
            String mapKey = effectKeyword;
            String hashKey = hashEffectMap.get(mapKey);
            Float mapValue = 1F;
            int multipleIndex = effectKeyword.indexOf("*100");
            if (multipleIndex != -1) {
                mapKey = effectKeyword.substring(0, multipleIndex);
                hashKey = hashEffectMap.get(mapKey);
                mapValue *= 100;
            }
            mapValue *= hashEffects.get(hashKey) == null ? 0F : hashEffects.get(hashKey);

            this.desc = this.desc.replace("@" + effectKeyword + "@", String.valueOf(mapValue));
        }
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

    private List<String> getDeleteMultipleByEffectKeywords(List<String> effectKeywords) {
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
