package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

@Data
public class SummonerDetailDTO {

    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private Long profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

}
