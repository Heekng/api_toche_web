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
    @Column(name = "rank")
    private Integer rank;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tft_match_id", nullable = false)
    private TftMatch tftMatch;

    @OneToMany(mappedBy = "matchInfo", cascade = ALL)
    private List<MatchUnit> matchUnits = new ArrayList<>();
    @OneToMany(mappedBy = "matchInfo", cascade = ALL)
    private List<MatchTrait> matchTraits = new ArrayList<>();
    @OneToMany(mappedBy = "matchInfo", cascade = ALL)
    private List<MatchAugment> matchAugments = new ArrayList<>();

    @Builder
    public MatchInfo(LocalDateTime gameDatetime, Integer rank, Season season, TftMatch tftMatch) {
        this.gameDatetime = gameDatetime;
        this.rank = rank;
        this.season = season;
        this.tftMatch = tftMatch;
    }
}
