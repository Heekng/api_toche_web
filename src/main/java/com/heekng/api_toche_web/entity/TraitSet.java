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
public class TraitSet {

    @Id
    @GeneratedValue
    @Column(name = "trait_set_id")
    private Long traitSetId;
    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    private TraitStyle style;
    @Column(name = "min")
    private Integer min;
    @Column(name = "max")
    private Integer max;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trait_id", nullable = false)
    private Trait trait;

    @Builder
    public TraitSet(TraitStyle style, Integer min, Integer max, Trait trait) {
        this.style = style;
        this.min = min;
        this.max = max;
        this.trait = trait;
    }
}
