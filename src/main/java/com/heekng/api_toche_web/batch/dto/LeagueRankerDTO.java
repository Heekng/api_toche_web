package com.heekng.api_toche_web.batch.dto;

import com.heekng.api_toche_web.entity.Summoner;
import lombok.Data;

@Data
public class LeagueRankerDTO {

    private String summonerId;
    private String summonerName;
    private Integer leaguePoints;
    private String rank;
    private Integer wins;
    private Integer losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;

    public Summoner toSummonerEntity() {
        return Summoner.builder()
                .name(this.getSummonerName())
                .id(this.getSummonerId())
                .build();
    }
}
