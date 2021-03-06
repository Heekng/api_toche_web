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
    @Column(name = "ranking")
    private Integer ranking;
    @Column(name = "is_deck_collected")
    private Boolean isDeckCollected;

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
    public MatchInfo(LocalDateTime gameDatetime, Integer ranking, Season season, TftMatch tftMatch) {
        this.gameDatetime = gameDatetime;
        this.ranking = ranking;
        this.season = season;
        this.tftMatch = tftMatch;
    }

    public void addMatchUnit(MatchUnit matchUnit) {
        matchUnit.changeMatchInfo(this);
        this.matchUnits.add(matchUnit);
    }
    public void addMatchAugment(MatchAugment matchAugment) {
        matchAugment.changeMatchInfo(this);
        this.matchAugments.add(matchAugment);
    }
    public void addMatchTrait(MatchTrait matchTrait) {
        matchTrait.changeMatchInfo(this);
        this.matchTraits.add(matchTrait);
    }
    public void updateIsDeckCollectedByIsDeckCollected(Boolean isDeckCollected) {
        this.isDeckCollected = isDeckCollected;
    }
}
