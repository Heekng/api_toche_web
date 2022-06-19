package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;

@Data
public class CDragonAbilityDTO {

    private String desc;
    private String icon;
    private String name;
    private List<CDragonChampionAbilityVariableDTO> variables;
}
