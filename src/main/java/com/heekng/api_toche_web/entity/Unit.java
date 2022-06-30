package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "season_id"}
                )
        }
)
public class Unit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long id;
    @Column(name = "rarity")
    private Integer rarity;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "tier")
    private Integer tier;
    @Column(name = "cost")
    private Integer cost;

    @Column(name = "icon_path")
    private String iconPath;
    @Column(name = "kr_name")
    private String krName;
    @Column(name = "is_display")
    private Boolean isDisplay;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "unit", cascade = ALL)
    private List<UnitTrait> unitTraits = new ArrayList<>();
    @OneToMany(mappedBy = "unit")
    private List<MatchUnit> matchUnits = new ArrayList<>();
    @OneToMany(mappedBy = "unit", cascade = ALL)
    private List<Stat> stats = new ArrayList<>();
    @OneToMany(mappedBy = "unit", cascade = ALL)
    private List<Ability> abilities = new ArrayList<>();
    @OneToMany(mappedBy = "unit", cascade = ALL)
    private List<UseDeckUnit> useDeckUnits = new ArrayList<>();


    @Builder
    public Unit(Integer rarity, String name, Integer tier, Integer cost, String iconPath, String krName, Season season) {
        this.rarity = rarity;
        this.name = name;
        this.tier = tier;
        this.cost = cost;
        this.iconPath = iconPath;
        this.krName = krName;
        this.season = season;

        this.isDisplay = true;
    }

    public void addUnitTrait(UnitTrait unitTrait) {
        unitTrait.updateUnit(this);
        this.unitTraits.add(unitTrait);
    }

    public void addStat(Stat stat) {
        stat.updateUnit(this);
        this.stats.add(stat);
    }

    public void addAbility(Ability ability) {
        ability.updateUnit(this);
        this.abilities.add(ability);
    }

    public void updateByCDragonData(Integer cost, String iconPath, String krName) {
        this.cost = cost;
        this.krName = krName;
        if (!StringUtils.hasText(this.iconPath)) {
            this.iconPath = iconPath;
        }
    }
}
