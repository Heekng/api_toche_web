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

    @OneToOne(mappedBy = "tftMatch", fetch = LAZY, cascade = REMOVE)
    private MatchInfo matchInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "summoner_id", nullable = false)
    private Summoner summoner;

    @Builder
    public TftMatch(Long id, String matchId, MatchInfo matchInfo, Summoner summoner) {
        this.id = id;
        this.matchId = matchId;
        this.matchInfo = matchInfo;
        this.summoner = summoner;
    }
}
