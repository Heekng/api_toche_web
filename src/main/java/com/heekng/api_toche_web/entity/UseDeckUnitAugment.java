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
public class UseDeckUnitAugment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_deck_unit_augment_id")
    private Long id;
    @Column(name = "use_count")
    private Long useCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_deck_unit_id", nullable = false)
    private UseDeckUnit useDeckUnit;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_deck_augment_id", nullable = false)
    private UseDeckAugment useDeckAugment;

    @Builder
    public UseDeckUnitAugment(Long useCount, UseDeckUnit useDeckUnit, UseDeckAugment useDeckAugment) {
        this.useCount = useCount;
        this.useDeckUnit = useDeckUnit;
        this.useDeckAugment = useDeckAugment;
    }

    public void addUseCount() {
        this.useCount++;
    }
}
