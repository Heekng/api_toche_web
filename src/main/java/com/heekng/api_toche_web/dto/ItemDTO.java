package com.heekng.api_toche_web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class ItemDTO {

    @Data
    public static class ItemsRequest {

        private Integer itemNum;
        private String itemName;
        private Long seasonId;

        @Builder
        public ItemsRequest(Integer itemNum, String itemName, Long seasonId) {
            this.itemNum = itemNum;
            this.itemName = itemName;
            this.seasonId = seasonId;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemsResponse {

        private Long id;
        private Integer num;
        private String name;
        private String desc;
        private String krName;
        private Integer fromItem1;
        private Integer fromItem2;
        private Boolean isUnique;
        private String iconPath;

        @QueryProjection
        public ItemsResponse(Long id, Integer num, String name, String desc, String krName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath) {
            this.id = id;
            this.num = num;
            this.name = name;
            this.desc = desc;
            this.krName = krName;
            this.fromItem1 = fromItem1;
            this.fromItem2 = fromItem2;
            this.isUnique = isUnique;
            this.iconPath = iconPath;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemDetailResponse {

        private Long id;
        private Integer num;
        private String name;
        private String desc;
        private String krName;
        private ItemsResponse fromItem1;
        private ItemsResponse fromItem2;
        private Boolean isUnique;
        private String iconPath;
        private Long usedCount;

        @QueryProjection
        public ItemDetailResponse(Long id, Integer num, String name, String desc, String krName, ItemsResponse fromItem1, ItemsResponse fromItem2, Boolean isUnique, String iconPath) {
            this.id = id;
            this.num = num;
            this.name = name;
            this.desc = desc;
            this.krName = krName;
            this.fromItem1 = fromItem1;
            this.fromItem2 = fromItem2;
            this.isUnique = isUnique;
            this.iconPath = iconPath;
        }
    }

    @Data
    @NoArgsConstructor
    public static class MostUnitRequest {

        @NotNull(message = "seasonId is not null")
        private Long seasonId;

    }

    @Data
    @NoArgsConstructor
    public static class UnitRankResponse {

        private Long id;
        private Integer num;
        private String name;
        private String desc;
        private String krName;
        private Integer fromItem1;
        private Integer fromItem2;
        private Boolean isUnique;
        private String iconPath;
        private Long championUsedCount;

        @QueryProjection
        public UnitRankResponse(Long id, Integer num, String name, String desc, String krName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath, Long championUsedCount) {
            this.id = id;
            this.num = num;
            this.name = name;
            this.desc = desc;
            this.krName = krName;
            this.fromItem1 = fromItem1;
            this.fromItem2 = fromItem2;
            this.isUnique = isUnique;
            this.iconPath = iconPath;
            this.championUsedCount = championUsedCount;
        }
    }
}
