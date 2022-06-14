package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

import java.util.List;

@Data
public class UnitDTO {

    private String character_id;
    private List<String> itemNames;
    private List<Integer> items;
    private String name;
    private Integer rarity;
    private Integer tier;

}
