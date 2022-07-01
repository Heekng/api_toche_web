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
public class UseAugment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_augment")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "augment_id", nullable = false)
    private Augment augment;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_deck_augment_id", nullable = false)
    private UseDeckAugment useDeckAugment;

    @Builder
    public UseAugment(Augment augment, UseDeckAugment useDeckAugment) {
        this.augment = augment;
        this.useDeckAugment = useDeckAugment;
    }

    public void updateUseDeckAugment(UseDeckAugment useDeckAugment) {
        this.useDeckAugment = useDeckAugment;
    }
}
