package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.Unit;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TraitDTO {

    @Data
    public static class TraitsRequest {

        private String traitName;
        private Long seasonId;

        @Builder
        public TraitsRequest(String traitName, Long seasonId) {
            this.traitName = traitName;
            this.seasonId = seasonId;
        }
    }

    @Data
    @NoArgsConstructor
    public static class TraitsResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer tierTotalCount;
        private String desc;
        private String iconPath;
        private SeasonDTO.SeasonsResponse season;

    }

    @Data
    @NoArgsConstructor
    public static class UnitTraitResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer tierTotalCount;
        private String desc;
        private String iconPath;

        public UnitTraitResponse(Trait trait) {
            this.id = trait.getId();
            this.name = trait.getName();
            this.krName = trait.getKrName();
            this.tierTotalCount = trait.getTierTotalCount();
            this.desc = trait.getName();
            this.iconPath = trait.getIconPath();
        }
    }
}
