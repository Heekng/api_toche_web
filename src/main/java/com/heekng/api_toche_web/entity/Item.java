package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

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
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
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
