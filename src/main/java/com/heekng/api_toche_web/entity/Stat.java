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
public class Stat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "stat_value")
    private Float statValue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Builder
    public Stat(String name, Float statValue, Unit unit) {
        this.name = name;
        this.statValue = statValue;
        this.unit = unit;
    }

    public void updateUnit(Unit unit) {
        this.unit = unit;
    }

    public void updateStat(String name, Float statValue) {
        this.name = name;
        this.statValue = statValue;
    }
}
