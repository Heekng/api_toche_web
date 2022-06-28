package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Trait;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Data
    @NoArgsConstructor
    public static class TraitDetailResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer tierTotalCount;
        private String desc;
        private String iconPath;
        private SeasonDTO.SeasonsResponse season;
        private List<UnitDTO.TraitDetailResponse> units = new ArrayList<>();

        public void insertUnits(List<UnitDTO.TraitDetailResponse> units) {
            this.units.addAll(units);
        }
    }

}
