package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Season;
import lombok.Builder;
import lombok.Data;

public class SeasonDTO {

    @Data
    public static class SeasonsRequest {

        private Integer seasonNum;
        private String seasonName;

        @Builder
        public SeasonsRequest(Integer seasonNum, String seasonName) {
            this.seasonNum = seasonNum;
            this.seasonName = seasonName;
        }
    }

    @Data
    public static class SeasonsResponse {

        private Long seasonId;
        private Integer seasonNum;
        private String seasonName;

        public SeasonsResponse(Season season) {
            this.seasonId = season.getId();
            this.seasonNum = season.getSeasonNum();
            this.seasonName = season.getSeasonName();
        }
    }



}
