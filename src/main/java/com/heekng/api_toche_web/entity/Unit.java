package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Unit {

    @Id
    @GeneratedValue
    @Column(name = "unit_id")
    private Long unitId;
    @Column(name = "rarity")
    private Integer rarity;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "tier", nullable = false)
    private Integer tier;

    @OneToMany(mappedBy = "unit")
    private List<MatchUnit> matchUnits = new ArrayList<>();

    @Builder
    public Unit(Integer rarity, String name, Integer tier) {
        this.rarity = rarity;
        this.name = name;
        this.tier = tier;
    }
}
