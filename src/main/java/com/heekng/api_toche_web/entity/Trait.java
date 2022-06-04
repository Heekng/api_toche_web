package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trait {

    @Id
    @GeneratedValue
    @Column(name = "trait_id")
    private Long traitsId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "tier_total_count", nullable = false)
    private Integer tierTotalCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "trait")
    private List<MatchTrait> matchTraits = new ArrayList<>();

    @Builder
    public Trait(String name, Integer tierTotalCount) {
        this.name = name;
        this.tierTotalCount = tierTotalCount;
    }
}
