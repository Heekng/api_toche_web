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
public class Ability extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ability_id")
    private Long id;

    @Column(name = "desc")
    private String desc;
    @Column(name = "icon_path")
    private String iconPath;
    @Column(name = "name")
    private String name;
    @Column(name = "krName")
    private String krName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Builder
    public Ability(String desc, String iconPath, String name, String krName, Unit unit) {
        this.desc = desc;
        this.iconPath = iconPath;
        this.name = name;
        this.krName = krName;
        this.unit = unit;
    }

    public void updateUnit(Unit unit) {
        this.unit = unit;
    }
}
