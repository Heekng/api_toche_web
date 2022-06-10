package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Unit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long unitId;
    @Column(name = "rarity")
    private Integer rarity;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "tier", nullable = false)
    private Integer tier;
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "unit", cascade = ALL)
    private List<UnitTrait> unitTraits = new ArrayList<>();
    @OneToMany(mappedBy = "unit")
    private List<MatchUnit> matchUnits = new ArrayList<>();


    @Builder
    public Unit(Integer rarity, String name, Integer tier, Season season, Integer cost) {
        this.rarity = rarity;
        this.name = name;
        this.tier = tier;
        this.season = season;
        this.cost = cost;
    }

    public void addUnitTraits(List<UnitTrait> unitTraits) {
        this.unitTraits.addAll(unitTraits);
    }
}
