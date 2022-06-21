package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ItemDTO {

    @Data
    public static class ItemsRequest {

        private Integer itemNum;
        private String itemName;

        @Builder
        public ItemsRequest(Integer itemNum, String itemName) {
            this.itemNum = itemNum;
            this.itemName = itemName;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemsResponse {

        private Long id;
        private Integer num;
        private String name;
        private String desc;
        private String korName;
        private Integer fromItem1;
        private Integer fromItem2;
        private Boolean isUnique;
        private String iconPath;

        @Builder
        public ItemsResponse(Long id, Integer num, String name, String desc, String korName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath) {
            this.id = id;
            this.num = num;
            this.name = name;
            this.desc = desc;
            this.korName = korName;
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
        private String korName;
        private Integer fromItem1;
        private Integer fromItem2;
        private Boolean isUnique;
        private String iconPath;

        @Builder
        public ItemDetailResponse(Long id, Integer num, String name, String desc, String korName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath) {
            this.id = id;
            this.num = num;
            this.name = name;
            this.desc = desc;
            this.korName = korName;
            this.fromItem1 = fromItem1;
            this.fromItem2 = fromItem2;
            this.isUnique = isUnique;
            this.iconPath = iconPath;
        }
    }
}
