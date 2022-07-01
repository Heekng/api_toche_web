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
public class UseUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_unit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_deck_unit_id", nullable = false)
    private UseDeckUnit useDeckUnit;

    @Builder
    public UseUnit(Unit unit, UseDeckUnit useDeckUnit) {
        this.unit = unit;
        this.useDeckUnit = useDeckUnit;
    }

    public void updateUseDeckUnit(UseDeckUnit useDeckUnit) {
        this.useDeckUnit = useDeckUnit;
    }
}
