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
public class MatchAugment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_augment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_info_id", nullable = false)
    private MatchInfo matchInfo;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "augment_id", nullable = false)
    private Augment augment;

    @Builder
    public MatchAugment(MatchInfo matchInfo, Augment augment) {
        this.matchInfo = matchInfo;
        this.augment = augment;
    }
}
