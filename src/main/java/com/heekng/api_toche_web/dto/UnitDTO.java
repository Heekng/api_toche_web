package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Season;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UnitDTO {

    @Data
    public static class UnitsRequest {

        private Long seasonId;
        private String unitName;

        @Builder
        public UnitsRequest(Long seasonId, String unitName) {
            this.seasonId = seasonId;
            this.unitName = unitName;
        }

    }

    @Data
    @NoArgsConstructor
    public static class UnitsResponse {
        private Long unitId;
        private String unitName;
        private SeasonDTO.SeasonsResponse season;

        @Builder
        public UnitsResponse(Long unitId, String unitName, SeasonDTO.SeasonsResponse season) {
            this.unitId = unitId;
            this.unitName = unitName;
            this.season = season;
        }
    }

}
