package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Data
    public static class GuidRequest {
        private List<Long> unitIds;

        @Builder
        public GuidRequest(List<Long> unitIds) {
            this.unitIds = unitIds;
        }
    }

    @Data
    public static class GuidResultResponse {
        private List<UnitDTO.UnitsResponse> units;
        private Long resultCount;

        @Builder
        public GuidResultResponse(List<UnitsResponse> units, Long resultCount) {
            this.units = units;
            this.resultCount = resultCount;
        }
    }

}
