package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchTrait {

    @Id
    @GeneratedValue
    @Column(name = "match_trait_id")
    private Long matchTraitId;
    @Column(name = "tier_applied_count", nullable = false)
    private Integer tierAppliedCount;
    @Column(name = "style")
    private Integer style;
    @Column(name = "unit_count", nullable = false)
    private Integer unitCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_info_id", nullable = false)
    private MatchInfo matchInfo;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trait_id", nullable = false)
    private Trait trait;

    @Builder
    public MatchTrait(Integer tierAppliedCount, Integer style, Integer unitCount, MatchInfo matchInfo, Trait trait) {
        this.tierAppliedCount = tierAppliedCount;
        this.style = style;
        this.unitCount = unitCount;
        this.matchInfo = matchInfo;
        this.trait = trait;
    }
}
