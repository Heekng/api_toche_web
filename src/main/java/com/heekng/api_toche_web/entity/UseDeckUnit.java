package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseDeckUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_deck_unit_id")
    private Long id;
    @Column(name = "use_count")
    private Long useCount;

    @OneToMany(mappedBy = "useDeckUnit", cascade = PERSIST)
    private List<UseUnit> useUnits = new ArrayList<>();
    @OneToMany(mappedBy = "useDeckUnit")
    private List<UseDeckUnitAugment> useDeckUnitAugments = new ArrayList<>();

    @Builder
    public UseDeckUnit(Long useCount) {
        this.useCount = useCount;
    }

    public void insertUseUnit(UseUnit useUnit) {
        useUnit.updateUseDeckUnit(this);
        this.useUnits.add(useUnit);
    }

    public void addUseCount() {
        this.useCount++;
    }
}
