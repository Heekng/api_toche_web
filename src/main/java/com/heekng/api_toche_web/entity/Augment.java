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
public class Augment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "augment_id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private Integer num;
    @Column(name = "augment_desc")
    private String augmentDesc;
    @Column(name = "kr_name")
    private String krName;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "is_unique")
    private Boolean isUnique;
    @Column(name = "icon_path")
    private String iconPath;

    @OneToMany(mappedBy = "augment")
    private List<MatchAugment> matchAugments = new ArrayList<>();
    @OneToMany(mappedBy = "augment")
    private List<SeasonAugment> seasonAugments = new ArrayList<>();

    @Builder
    public Augment(String name, Integer num, String augmentDesc, String krName, String enName, Boolean isUnique, String iconPath) {
        this.name = name;
        this.num = num;
        this.augmentDesc = augmentDesc;
        this.krName = krName;
        this.enName = enName;
        this.isUnique = isUnique;
        this.iconPath = iconPath;
    }

    public void updateByCDragonData(Integer num, String augmentDesc, String krName, String enName, Boolean isUnique, String iconPath) {
        this.num = num;
        this.augmentDesc = augmentDesc;
        this.krName = krName;
        this.enName = enName;
        this.isUnique = isUnique;
        if (!StringUtils.hasText(this.iconPath)) {
            this.iconPath = iconPath;
        }
    }

    public void updateName(String name) {
        this.name = name;
    }
}
