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

    @Column(name = "desc")
    private String desc;
    @Column(name = "kor_name")
    private String korName;
    @Column(name = "from_item_1")
    private Integer fromItem1;
    @Column(name = "from_item_2")
    private Integer fromItem2;
    @Column(name = "is_unique")
    private Boolean isUnique;
    @Column(name = "icon_path")
    private String iconPath;

    @OneToMany(mappedBy = "item")
    private List<MatchItem> matchItems = new ArrayList<>();

    @Builder
    public Item(Integer num, String name, String desc, String korName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath) {
        this.num = num;
        this.name = name;
        this.desc = desc;
        this.korName = korName;
        this.fromItem1 = fromItem1;
        this.fromItem2 = fromItem2;
        this.isUnique = isUnique;
        this.iconPath = iconPath;
    }

    public void updateCDragonData(String desc, String korName, Boolean isUnique, String iconPath, Integer fromItem1, Integer fromItem2) {
        this.desc = desc;
        this.korName = korName;
        this.isUnique = isUnique;
        this.iconPath = iconPath;
        this.fromItem1 = fromItem1;
        this.fromItem2 = fromItem2;
    }
}
