package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;

@Data
public class CDragonChampionDTO {

    private CDragonAbilityDTO ability;
    private String apiName;
    private Integer cost;
    private String icon;
    private String name;
    private CDragonChampionStatDTO stats;
    private List<String> traits;
}
