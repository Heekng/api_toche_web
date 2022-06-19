package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;

@Data
public class CDragonSetDataDTO {

    private List<CDragonChampionDTO> champions;
    private String mutator;
    private String name;
    private Integer number;
    private List<CDragonTraitDTO> traits;
}
