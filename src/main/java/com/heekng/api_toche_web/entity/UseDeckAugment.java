package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseDeckAugment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_deck_augment_id")
    private Long id;

    @Column(name = "use_count")
    private Long useCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @OneToMany(mappedBy = "useDeckAugment", fetch = LAZY, cascade = PERSIST)
    private List<UseAugment> useAugments = new ArrayList<>();
    @OneToMany(mappedBy = "useDeckAugment", cascade = ALL)
    private List<UseDeckUnitAugment> useDeckUnitAugments = new ArrayList<>();

    @Builder
    public UseDeckAugment(Long useCount, Season season) {
        this.useCount = useCount;
        this.season = season;
    }

    public void insertUseAugment(UseAugment useAugment) {
        useAugment.updateUseDeckAugment(this);
        this.useAugments.add(useAugment);
    }

    public void addUseCount() {
        this.useCount++;
    }
}
