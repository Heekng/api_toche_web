package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"num", "name"}
                )
        }
)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "num", nullable = false)
    private Integer num;
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "item")
    private List<MatchItem> matchItems = new ArrayList<>();

    @Builder
    public Item(Integer num, String name) {
        this.num = num;
        this.name = name;
    }

}
