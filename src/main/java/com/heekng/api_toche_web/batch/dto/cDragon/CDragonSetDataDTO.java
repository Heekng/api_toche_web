package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CDragonSetDataDTO {

    private List<CDragonChampionDTO> champions;
    private String mutator;
    private String name;
    private Integer number;
    private List<CDragonTraitDTO> traits;

    public Boolean isMutatorHasKeywordsLeastOne(List<String> keywords) {
        for (String keyword : keywords) {
            if ( this.mutator.contains(keyword) ) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getChampionApiNameAndAbilityNameMap() {
        return this.champions.stream()
                .collect(
                        Collectors.toMap(CDragonChampionDTO::getApiName, cDragonChampionDTO -> cDragonChampionDTO.getAbility().getNullSafeName())
                );
    }

    public void updateChampionsAbilityEnNameByApiNameAndEnNameMap(Map<String, String> championApiNameAndEnNameMap) {
        this.champions.forEach(cDragonChampionDTO -> {
            cDragonChampionDTO.getAbility()
                    .setEnName(championApiNameAndEnNameMap.get(cDragonChampionDTO.getApiName()));
        });
    }
}
