package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;

@Data
public class CDragonTraitDTO {

    private String apiName;
    private String desc;
    private List<CDragonTraitEffectDTO> effects;
    private String icon;
    private String name;

}
