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

        private Long traitId;
        private String traitName;
        private Integer tierTotalCount;
        private SeasonDTO.SeasonsResponse season;

        @Builder
        public TraitsResponse(Long traitId, String traitName, Integer tierTotalCount, SeasonDTO.SeasonsResponse season) {
            this.traitId = traitId;
            this.traitName = traitName;
            this.tierTotalCount = tierTotalCount;
            this.season = season;
        }
    }
}
