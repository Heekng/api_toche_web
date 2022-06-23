package com.heekng.api_toche_web.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    @Data
    @NoArgsConstructor
    public static class ItemRankResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer rarity;
        private Integer tier;
        private Integer cost;
        private String iconPath;
        private Long itemUsedCount;

        @QueryProjection
        public ItemRankResponse(Long id, String name, String krName, Integer rarity, Integer tier, Integer cost, String iconPath, Long itemUsedCount) {
            this.id = id;
            this.name = name;
            this.krName = krName;
            this.rarity = rarity;
            this.tier = tier;
            this.cost = cost;
            this.iconPath = iconPath;
            this.itemUsedCount = itemUsedCount;
        }
    }

}
