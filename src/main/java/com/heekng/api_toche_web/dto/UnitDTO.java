package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Stat;
import com.heekng.api_toche_web.entity.Unit;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Data
    @NoArgsConstructor
    public static class UnitDetailResponse {

        private Long id;
        private String name;
        private String krName;
        private Integer rarity;
        private Integer tier;
        private Integer cost;
        private String iconPath;
        private SeasonDTO.SeasonsResponse season;
        private AbilityDTO.AbilityResponse ability;
        private Map<String, Float> stats = new HashMap<>();
        private List<TraitDTO.UnitTraitResponse> traits = new ArrayList<>();

        public UnitDetailResponse(Unit unit) {
            this.id = unit.getId();
            this.name = unit.getName();
            this.krName = unit.getKrName();
            this.rarity = unit.getRarity();
            this.tier = unit.getTier();
            this.cost = unit.getCost();
            this.iconPath = unit.getIconPath();
            this.season = new SeasonDTO.SeasonsResponse(unit.getSeason());
            if (!unit.getAbilities().isEmpty()) {
                this.ability = new AbilityDTO.AbilityResponse(unit.getAbilities().get(0));
            }
            if (!unit.getStats().isEmpty()) {
                this.stats = unit.getStats().stream()
                        .collect(Collectors.toMap(Stat::getName, Stat::getStatValue));
            }
            if (!unit.getUnitTraits().isEmpty()) {
                unit.getUnitTraits().forEach(unitTrait -> {
                    this.traits.add(new TraitDTO.UnitTraitResponse(unitTrait.getTrait()));
                });
            }
        }
    }

}
