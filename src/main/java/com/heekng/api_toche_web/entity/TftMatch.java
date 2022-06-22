package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"summoner_id", "match_id"}
                )
        }
)
public class TftMatch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tft_match_id")
    private Long id;
    @Column(name = "match_id", nullable = false)
    private String matchId;
    @Column(name = "game_type")
    private String gameType;

    @OneToMany(mappedBy = "tftMatch", fetch = LAZY, cascade = REMOVE)
    private List<MatchInfo> matchInfos;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "summoner_id", nullable = false)
    private Summoner summoner;

    @Builder
    public TftMatch(Long id, String matchId, String gameType, Summoner summoner) {
        this.id = id;
        this.matchId = matchId;
        this.gameType = gameType;
        this.summoner = summoner;
    }

    public void updateGameType(String gameType) {
        this.gameType = gameType;
    }
}
