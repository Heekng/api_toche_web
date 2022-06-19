package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ItemDTO {

    @Data
    public static class ItemsRequest {

        private Long seasonId;
        private Integer itemNum;
        private String itemName;

        @Builder
        public ItemsRequest(Long seasonId, Integer itemNum, String itemName) {
            this.seasonId = seasonId;
            this.itemNum = itemNum;
            this.itemName = itemName;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ItemsResponse {

        private Long itemId;
        private Integer itemNum;
        private String itemName;

        @Builder
        public ItemsResponse(Long itemId, Integer itemNum, String itemName) {
            this.itemId = itemId;
            this.itemNum = itemNum;
            this.itemName = itemName;
        }
    }
}
