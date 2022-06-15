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
        private String seasonName;
        private Integer seasonNum;

        @Builder
        public UnitsResponse(Long unitId, String unitName, String seasonName, Integer seasonNum) {
            this.unitId = unitId;
            this.unitName = unitName;
            this.seasonName = seasonName;
            this.seasonNum = seasonNum;
        }
    }

}
