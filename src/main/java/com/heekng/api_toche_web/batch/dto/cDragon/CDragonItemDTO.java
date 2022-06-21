package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
}
