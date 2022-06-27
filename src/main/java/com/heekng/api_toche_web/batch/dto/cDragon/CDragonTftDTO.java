package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CDragonTftDTO {

    private List<CDragonItemDTO> items;
    private List<CDragonSetDataDTO> setData;
    private Map<String, CDragonSetDataDTO> sets;

    public void updateSetDataInsertableValueByKeywordList(List<String> keywords) {
        updateSetDataMutatorToCommonName();
        deleteSetDataByKeywordList(keywords);
    }

    private void updateSetDataMutatorToCommonName() {
        this.getSetData()
                .forEach(cDragonSetDataDTO -> {
                    cDragonSetDataDTO.setMutator(cDragonSetDataDTO.getMutator().replace("TFT_Set", "TFTSet"));
                    cDragonSetDataDTO.setMutator(cDragonSetDataDTO.getMutator().replace("Act", "Stage"));
                });
    }

    private void deleteSetDataByKeywordList(List<String> keywords) {
        for (int i = 0; i < this.getSetData().size(); i++) {
            CDragonSetDataDTO cDragonSetDataDTO = this.getSetData().get(i);
            if (cDragonSetDataDTO.isMutatorHasKeywordsLeastOne(keywords)) {
                this.getSetData().remove(i);
                i--;
            }
        }
    }

    public Map<String, Map<String, String>> getMutatorAndApiNameAndAbilityNameMap() {
        return this.getSetData().stream()
                .collect(
                        Collectors.toMap(
                                CDragonSetDataDTO::getMutator,
                                CDragonSetDataDTO::getChampionApiNameAndAbilityNameMap
                        )
                );
    }

    public void updateSetDataEnNameByMutatorAndApiNameAndAbilityNameMap(Map<String, Map<String, String>> mutatorAndApiNameAndAbilityNameMap) {
        this.setData.forEach(cDragonSetDataDTO -> {
            Map<String, String> apiNameAndEnNameMap = mutatorAndApiNameAndAbilityNameMap.get(cDragonSetDataDTO.getMutator());
            cDragonSetDataDTO.updateChampionsAbilityEnNameByApiNameAndEnNameMap(apiNameAndEnNameMap);
        });
    }
}
