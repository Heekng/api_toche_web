package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LeagueListDTO {

    private String tier;
    private String leagueId;
    private String queue;
    private String name;
    private List<LeagueRankerDTO> entries = new ArrayList<>();

}
