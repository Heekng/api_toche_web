package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.Map;

@Data
public class CDragonTraitEffectDTO {

    private Integer maxUnits;
    private Integer minUnits;
    private Integer style;
    private Map<String, Float> variables;
}
