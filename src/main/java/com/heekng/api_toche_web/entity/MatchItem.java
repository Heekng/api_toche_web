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
public class MatchItem {

    @Id
    @GeneratedValue
    @Column(name = "match_item_id")
    private Long matchItemId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_unit_id")
    private MatchUnit matchUnit;

    @Builder
    public MatchItem(Item item, MatchUnit matchUnit) {
        this.item = item;
        this.matchUnit = matchUnit;
    }
}
