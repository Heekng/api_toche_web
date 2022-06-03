package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchUnit extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "match_unit_id")
    private Long matchUnitId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_info_id", nullable = false)
    private MatchInfo matchInfo;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @OneToMany(mappedBy = "matchUnit", cascade = REMOVE)
    private List<MatchItem> matchItems = new ArrayList<>();

    @Builder
    public MatchUnit(MatchInfo matchInfo, Unit unit) {
        this.matchInfo = matchInfo;
        this.unit = unit;
    }
}
