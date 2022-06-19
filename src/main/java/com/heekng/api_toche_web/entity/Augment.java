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
public class Augment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "augment_id")
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "augment")
    private List<MatchAugment> matchAugments = new ArrayList<>();

    @Builder
    public Augment(String name) {
        this.name = name;
    }
}
