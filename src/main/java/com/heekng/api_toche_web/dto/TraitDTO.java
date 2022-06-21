package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TraitDTO {

    @Data
    public static class TraitsRequest {

        private String traitName;
        private Long seasonId;

        @Builder
        public TraitsRequest(String traitName, Long seasonId) {
            this.traitName = traitName;
            this.seasonId = seasonId;
        }
    }

    @Data
    @NoArgsConstructor
    public static class TraitsResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer tierTotalCount;
        private String desc;
        private String iconPath;
        private SeasonDTO.SeasonsResponse season;

    }
}
