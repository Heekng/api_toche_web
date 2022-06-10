package com.heekng.api_toche_web.entity;

import com.heekng.api_toche_web.enums.TraitStyle;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TraitSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
