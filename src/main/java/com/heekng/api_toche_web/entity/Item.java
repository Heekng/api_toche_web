package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "num", nullable = false)
    private Integer num;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "item_desc")
    private String itemDesc;
    @Column(name = "kr_name")
    private String krName;
    @Column(name = "from_item_1")
    private Integer fromItem1;
    @Column(name = "from_item_2")
    private Integer fromItem2;
    @Column(name = "is_unique")
    private Boolean isUnique;
    @Column(name = "icon_path")
    private String iconPath;
    @Column(name = "is_display")
    private Boolean isDisplay;

    @OneToMany(mappedBy = "item")
    private List<MatchItem> matchItems = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<SeasonItem> seasonItems = new ArrayList<>();

    @Builder
    public Item(Integer num, String name, String itemDesc, String krName, Integer fromItem1, Integer fromItem2, Boolean isUnique, String iconPath) {
        this.num = num;
        this.name = name;
        this.itemDesc = itemDesc;
        this.krName = krName;
        this.fromItem1 = fromItem1;
        this.fromItem2 = fromItem2;
        this.isUnique = isUnique;
        this.iconPath = iconPath;
        this.isDisplay = true;
    }

    public void updateByCDragonData(String itemDesc, String krName, Boolean isUnique, String iconPath, Integer fromItem1, Integer fromItem2) {
        this.itemDesc = itemDesc;
        this.krName = krName;
        this.isUnique = isUnique;
        this.fromItem1 = fromItem1;
        this.fromItem2 = fromItem2;
        if (!StringUtils.hasText(this.iconPath)) {
            this.iconPath = iconPath;
        }
    }

    public void updateName(String name) {
        this.name = name;
    }
}
