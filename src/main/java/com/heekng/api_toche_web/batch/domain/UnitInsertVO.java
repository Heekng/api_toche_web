package com.heekng.api_toche_web.batch.domain;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnitInsertVO {

    private String name;
    private String championId;
    private Integer cost;
    private List<String> traits = new ArrayList<>();

    @Builder
    public UnitInsertVO(String name, String championId, Integer cost, List<String> traits) {
        this.name = name;
        this.championId = championId;
        this.cost = cost;
        this.traits = traits;
    }

    public Unit toEntity(Season season) {
        return Unit.builder()
                .season(season)
                .name(this.name)
                .cost(this.cost)
                .tier(this.cost)
                .build();
    }
}
