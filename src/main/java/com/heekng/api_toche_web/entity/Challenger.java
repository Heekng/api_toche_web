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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"summoner_id", "challenger_inquiry_id"}
                )
        }
)
public class Challenger extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "challenger_id")
    private Long challengerId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "challenger_inquiry_id", nullable = false)
    private ChallengerInquiry challengerInquiry;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "summoner_id", nullable = false)
    private Summoner summoner;

    @Builder
    public Challenger(ChallengerInquiry challengerInquiry, Summoner summoner) {
        this.challengerInquiry = challengerInquiry;
        this.summoner = summoner;
    }
}
