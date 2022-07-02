package com.heekng.api_toche_web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

        private Long id;
        private String name;
        private Integer num;
        private String desc;
        private String krName;
        private String enName;
        private Boolean isUnique;
        private String iconPath;

    }

    @Data
    public static class GuidRequest {

        @NotNull
        @NotEmpty
        @Size(max = 3)
        private List<Long> augmentIds;

        @NotNull
        private Long seasonId;

        @Builder
        public GuidRequest(List<Long> augmentIds, Long seasonId) {
            this.augmentIds = augmentIds;
            this.seasonId = seasonId;
        }
    }

    @Data
    public static class GuidResultResponse {

        private List<UnitDTO.UnitsResponse> units;
        private Long resultCount;
        private Integer allUsedCount;

        @Builder
        public GuidResultResponse(List<UnitDTO.UnitsResponse> units, Long resultCount, Integer allUsedCount) {
            this.units = units;
            this.resultCount = resultCount;
            this.allUsedCount = allUsedCount;
        }
    }



}
