package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {

    @Id
    @GeneratedValue
    @Column(name = "match_id")
    private Long matchId;
    @Column(name = "victory_match_id")
    private String victoryMatchId;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "match_info_id")
    private MatchInfo matchInfo;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    @Builder
    public Match(Long matchId, String victoryMatchId, MatchInfo matchInfo, Summoner summoner) {
        this.matchId = matchId;
        this.victoryMatchId = victoryMatchId;
        this.matchInfo = matchInfo;
        this.summoner = summoner;
    }
}
