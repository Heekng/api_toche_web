package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
        
        private Long id;
        private String name;
        private String krName;
        private Integer rarity;
        private Integer tier;
        private Integer cost;
        private String iconPath;
        private SeasonDTO.SeasonsResponse season;

    }

    @Data
    public static class GuidRequest {
        @NotNull(message = "unitIds is not null")
        private List<Long> unitIds;

        @Builder
        public GuidRequest(List<Long> unitIds) {
            this.unitIds = unitIds;
        }
    }

}
