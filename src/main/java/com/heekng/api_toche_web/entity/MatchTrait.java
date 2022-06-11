package com.heekng.api_toche_web.entity;

import com.heekng.api_toche_web.enums.TraitStyle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchTrait extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_trait_id")
    private Long id;
    @Column(name = "tier_applied_count", nullable = false)
    private Integer tierAppliedCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    private TraitStyle style;
    @Column(name = "unit_count", nullable = false)
    private Integer unitCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_info_id", nullable = false)
    private MatchInfo matchInfo;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trait_id", nullable = false)
    private Trait trait;

    @Builder
    public MatchTrait(Integer tierAppliedCount, Integer styleNum, Integer unitCount, MatchInfo matchInfo, Trait trait) {
        this.tierAppliedCount = tierAppliedCount;
        this.style = TraitStyle.valueOf("_" + styleNum);
        this.unitCount = unitCount;
        this.matchInfo = matchInfo;
        this.trait = trait;
    }
}
