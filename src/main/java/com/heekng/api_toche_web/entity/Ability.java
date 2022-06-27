package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    @Column(name = "ability_desc")
    private String abilityDesc;
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
    public Ability(String abilityDesc, String iconPath, String name, String krName, Unit unit) {
        this.abilityDesc = abilityDesc;
        this.iconPath = iconPath;
        this.name = name;
        this.krName = krName;
        this.unit = unit;
    }

    public void updateUnit(Unit unit) {
        this.unit = unit;
    }

    public void updateByCDragonData(String abilityDesc, String iconPath, String name, String krName) {
        this.abilityDesc = abilityDesc;
        this.name = name;
        this.krName = krName;
        if (!StringUtils.hasText(this.iconPath)) {
            this.iconPath = iconPath;
        }
    }
}
