package com.heekng.api_toche_web.batch.domain;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.TraitSet;
import com.heekng.api_toche_web.enums.TraitStyle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TraitInsertVO {

    private String key;
    private String name;
    private String description;
    private String innate;
    private String type;
    private List<SetVO> sets = new ArrayList<>();

    @Builder
    public TraitInsertVO(String key, String name, String description, String type, List<SetVO> sets) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.type = type;
        this.sets = sets;
    }

    public Trait toEntity(Season season) {
        Trait trait = Trait.builder()
                .name(this.name.substring(this.name.indexOf("_") + 1))
                .season(season)
                .tierTotalCount(sets.size())
                .build();
        List<TraitSet> traitSetList = new ArrayList<>();
        TraitSet noTraitSet = TraitSet.builder()
                .style(TraitStyle._0)
                .trait(trait)
                .build();
        traitSetList.add(noTraitSet);
        for (SetVO set : sets) {
            TraitSet traitSet = TraitSet.builder()
                    .style(TraitStyle.findByStyleName(set.getStyle()))
                    .max(set.getMax())
                    .min(set.getMin())
                    .trait(trait)
                    .build();
            traitSetList.add(traitSet);
        }
        trait.addTraitSets(traitSetList);
        return trait;
    }
}
