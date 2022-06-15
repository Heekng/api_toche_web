package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AugmentDTO {

    @Data
    public static class AugmentsRequest {

        private String augmentName;
        private Long seasonId;

        @Builder
        public AugmentsRequest(String augmentName, Long seasonId) {
            this.augmentName = augmentName;
            this.seasonId = seasonId;
        }
    }

    @Data
    @NoArgsConstructor
    public static class AugmentsResponse {

        private Long augmentId;
        private String augmentName;
        private SeasonDTO.SeasonsResponse season;

        @Builder
        public AugmentsResponse(Long augmentId, String augmentName, SeasonDTO.SeasonsResponse season) {
            this.augmentId = augmentId;
            this.augmentName = augmentName;
            this.season = season;
        }
    }

}
