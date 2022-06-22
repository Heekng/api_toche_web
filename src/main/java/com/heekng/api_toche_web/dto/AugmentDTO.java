package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Unit;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
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
        private String korName;
        private String enName;
        private Boolean isUnique;
        private String iconPath;

    }

    @Data
    public static class GuidRequest {

        @NotNull
        @Size(max = 3)
        private List<Long> augmentIds;

        @Builder
        public GuidRequest(List<Long> augmentIds) {
            this.augmentIds = augmentIds;
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
