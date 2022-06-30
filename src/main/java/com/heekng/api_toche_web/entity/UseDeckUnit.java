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
public class UseDeckUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_deck_unit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_deck_id", nullable = false)
    private UseDeck useDeck;

    @Builder
    public UseDeckUnit(Unit unit, UseDeck useDeck) {
        this.unit = unit;
        this.useDeck = useDeck;
    }
}
