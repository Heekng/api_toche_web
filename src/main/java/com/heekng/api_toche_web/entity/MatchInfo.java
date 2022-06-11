package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_info_id")
    private Long id;
    @Column(name = "game_datetime", nullable = false)
    private LocalDateTime gameDatetime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @OneToMany(mappedBy = "matchInfo", cascade = REMOVE)
    private List<MatchUnit> matchUnits = new ArrayList<>();
    @OneToMany(mappedBy = "matchInfo", cascade = REMOVE)
    private List<MatchTrait> matchTraits = new ArrayList<>();

    @Builder
    public MatchInfo(LocalDateTime gameDatetime, Season season, Match match) {
        this.gameDatetime = gameDatetime;
        this.season = season;
        this.match = match;
    }
}
