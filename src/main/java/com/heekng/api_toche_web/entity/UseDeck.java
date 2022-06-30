package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseDeck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_deck_id")
    private Long id;
    @Column(name = "use_count")
    private Long useCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_deck_augment_id", nullable = false)
    private UseDeckAugment useDeckAugment;

    @OneToMany(mappedBy = "useDeck", cascade = ALL)
    private List<UseDeckUnit> useDeckUnits = new ArrayList<>();

    @Builder
    public UseDeck(Long useCount, UseDeckAugment useDeckAugment) {
        this.useCount = useCount;
        this.useDeckAugment = useDeckAugment;
    }
}
