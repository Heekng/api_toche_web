package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Season;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NoArgsConstructor
    public static class SeasonsResponse {

        private Long id;
        private Integer num;
        private String name;

    }



}
