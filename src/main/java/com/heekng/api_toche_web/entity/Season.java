package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "season_id")
    private Long seasonId;
    @Column(name = "season_num", nullable = false, unique = true)
    private String seasonNum;

    @OneToMany(mappedBy = "season", cascade = CascadeType.REMOVE)
    private List<MatchInfo> matchInfos = new ArrayList<>();

    @Builder
    public Season(String seasonNum) {
        this.seasonNum = seasonNum;
    }
}
