package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

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

    @OneToMany(mappedBy = "useDeckUnit", cascade = ALL)
    private List<UseUnit> useDeckUnits = new ArrayList<>();

    @Builder
    public UseDeckUnit(Long useCount) {
        this.useCount = useCount;
    }
}
