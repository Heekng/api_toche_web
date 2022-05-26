package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summoner {

    @Id
    @GeneratedValue
    @Column(name = "summoner_id")
    private Long summonerId;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "profile_icon_id")
    private Long profileIconId;
    @Column(name = "revision_datetime")
    private LocalDateTime revisionDatetime;
    @Column(name = "name")
    private String name;
    @Column(name = "id")
    private String id;
    @Column(name = "puuid")
    private String puuid;
    @Column(name = "summoner_level")
    private Long summonerLevel;

    @OneToMany(mappedBy = "summoner", cascade = REMOVE)
    private List<Challenger> challengers = new ArrayList<>();
    @OneToMany(mappedBy = "summoner", cascade = REMOVE)
    private List<Match> matches = new ArrayList<>();

    @Builder
    public Summoner(String accountId, Long profileIconId, LocalDateTime revisionDatetime, String name, String id, String puuid, Long summonerLevel) {
        this.accountId = accountId;
        this.profileIconId = profileIconId;
        this.revisionDatetime = revisionDatetime;
        this.name = name;
        this.id = id;
        this.puuid = puuid;
        this.summonerLevel = summonerLevel;
    }
}
