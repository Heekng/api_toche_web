package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeasonItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    Season season;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @Builder
    public SeasonItem(Season season, Item item) {
        this.season = season;
        this.item = item;
    }
}
