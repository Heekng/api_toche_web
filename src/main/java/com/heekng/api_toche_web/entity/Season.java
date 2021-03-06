package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"season_num", "season_name"}
                )
        }
)
public class Season extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Long id;
    @Column(name = "season_num", nullable = false)
    private Integer seasonNum;
    @Column(name = "season_name", nullable = false)
    private String seasonName;
    @Column(name = "last_patch_version")
    private String lastPatchVersion;
    @Column(name = "is_display")
    private Boolean isDisplay;

    @OneToMany(mappedBy = "season")
    private List<MatchInfo> matchInfos = new ArrayList<>();
    @OneToMany(mappedBy = "season")
    private List<Unit> units = new ArrayList<>();
    @OneToMany(mappedBy = "season")
    private List<Trait> traits = new ArrayList<>();
    @OneToMany(mappedBy = "season")
    private List<SeasonAugment> seasonAugments = new ArrayList<>();
    @OneToMany(mappedBy = "season")
    private List<SeasonItem> seasonItems = new ArrayList<>();
    @OneToMany(mappedBy = "season")
    private List<UseDeckAugment> useDeckAugments = new ArrayList<>();

    @Builder
    public Season(Integer seasonNum, String seasonName) {
        this.seasonNum = seasonNum;
        this.seasonName = seasonName;
        this.isDisplay = true;
    }
}
