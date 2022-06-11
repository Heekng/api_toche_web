package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"summoner_id", "victory_match_id"}
                )
        }
)
public class Match extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;
    @Column(name = "victory_match_id", nullable = false)
    private String victoryMatchId;

    @OneToOne(mappedBy = "match", fetch = LAZY, cascade = REMOVE)
    private MatchInfo matchInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "summoner_id", nullable = false)
    private Summoner summoner;

    @Builder
    public Match(Long matchId, String victoryMatchId, MatchInfo matchInfo, Summoner summoner) {
        this.id = matchId;
        this.victoryMatchId = victoryMatchId;
        this.matchInfo = matchInfo;
        this.summoner = summoner;
    }
}
