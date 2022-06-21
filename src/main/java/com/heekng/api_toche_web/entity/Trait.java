package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

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
public class Trait extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "tier_total_count", nullable = false)
    private Integer tierTotalCount;

    @Column(name = "desc")
    private String desc;
    @Column(name = "icon_path")
    private String iconPath;
    @Column(name = "kr_name")
    private String krName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "trait")
    private List<UnitTrait> unitTraits;
    @OneToMany(mappedBy = "trait")
    private List<MatchTrait> matchTraits = new ArrayList<>();
    @OneToMany(mappedBy = "trait", cascade = ALL)
    private List<TraitSet> traitSets = new ArrayList<>();

    @Builder
    public Trait(String name, Integer tierTotalCount, String desc, String iconPath, String krName, Season season) {
        this.name = name;
        this.tierTotalCount = tierTotalCount;
        this.desc = desc;
        this.iconPath = iconPath;
        this.krName = krName;
        this.season = season;
    }

    public void addTraitSets(List<TraitSet> traitSets) {
        this.traitSets.addAll(traitSets);
    }

    public void updateCDragonData(String desc, String iconPath, String krName) {
        this.desc = desc;
        this.iconPath = iconPath;
        this.krName = krName;
    }
}
