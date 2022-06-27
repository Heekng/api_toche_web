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

    @Column(name = "trait_desc")
    private String traitDesc;
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
    public Trait(String name, Integer tierTotalCount, String traitDesc, String iconPath, String krName, Season season) {
        this.name = name;
        this.tierTotalCount = tierTotalCount;
        this.traitDesc = traitDesc;
        this.iconPath = iconPath;
        this.krName = krName;
        this.season = season;
    }

    public void addTraitSets(List<TraitSet> traitSets) {
        this.traitSets.addAll(traitSets);
    }

    public void updateByCDragonData(String traitDesc, String iconPath, String krName) {
        this.traitDesc = traitDesc;
        this.krName = krName;
        if (!StringUtils.hasText(this.iconPath)) {
            this.iconPath = iconPath;
        }
    }
}
