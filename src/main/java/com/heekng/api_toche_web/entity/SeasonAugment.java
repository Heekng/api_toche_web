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
public class SeasonAugment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_augment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    Season season;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "augment_id", nullable = false)
    Augment augment;

    @Builder
    public SeasonAugment(Season season, Augment augment) {
        this.season = season;
        this.augment = augment;
    }

}
