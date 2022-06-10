package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnitTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_trait_id")
    private Long unitTraitId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trait_id", nullable = false)
    private Trait trait;

    @Builder
    public UnitTrait(Unit unit, Trait trait) {
        this.unit = unit;
        this.trait = trait;
    }
}
