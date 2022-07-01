package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class GuidDTO {

    @Data
    public static class GuidResultResponse {
        private List<UnitDTO.UnitsResponse> units;
        private Long resultCount;
        private Long allUsedCount;

        @Builder
        public GuidResultResponse(List<UnitDTO.UnitsResponse> units, Long resultCount, Long allUsedCount) {
            this.units = units;
            this.resultCount = resultCount;
            this.allUsedCount = allUsedCount;
        }
    }

}
