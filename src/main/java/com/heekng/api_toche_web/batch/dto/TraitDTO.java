package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

@Data
public class TraitDTO {

    private String name;
    private Integer num_units;
    private Integer style;
    private Integer tier_current;
    private Integer tier_total;

}
