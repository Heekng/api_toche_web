package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.AugmentRepository;
import com.heekng.api_toche_web.repository.SeasonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AugmentServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    AugmentService augmentService;
    @Autowired
    AugmentRepository augmentRepository;
    @Autowired
    SeasonRepository seasonRepository;

    Season season;
    Augment augment;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        seasonRepository.save(season);

        augment = Augment.builder()
                .name("augmentName")
                .season(season)
                .build();
        augmentRepository.save(augment);
    }

    @Test
    @DisplayName("이미 존재하는 특성은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Augment findAugment = augmentService.findOrSave(augment.getName(), season);
        //then
        assertThat(findAugment.getId()).isEqualTo(augment.getId());
    }

    @Test
    @DisplayName("존재하지 않는 특성은 새로 저장한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Augment findAugment = augmentService.findOrSave("testAugmentName", season);
        em.flush();
        em.clear();
        Optional<Augment> optionalAugment = augmentRepository.findById(findAugment.getId());
        //then
        assertThat(optionalAugment).isNotEmpty();
        assertThat(optionalAugment.get().getName()).isEqualTo(findAugment.getName());
        assertThat(optionalAugment.get().getSeason().getSeasonName()).isEqualTo(findAugment.getSeason().getSeasonName());

    }

    @Test
    @DisplayName("isExistUnits 는 존재하지 않는 유닛 ID일 경우 False를 리턴한다.")
    void isExistUnitsFalseTest() throws Exception {
        //given
        Augment testAugment1 = Augment.builder()
                .name("testAugmentName1")
                .season(season)
                .build();
        augmentRepository.save(testAugment1);
        Augment testAugment2 = Augment.builder()
                .name("testAugmentName2")
                .season(season)
                .build();
        augmentRepository.save(testAugment2);
        Augment testAugment3 = Augment.builder()
                .name("testAugmentName3")
                .season(season)
                .build();
        augmentRepository.save(testAugment3);
        //when
        List<Long> augmentIds = new ArrayList<>();
        augmentIds.add(testAugment1.getId());
        augmentIds.add(testAugment2.getId());
        augmentIds.add(100L);
        Boolean existUnits = augmentService.isExistUnits(augmentIds);
        //then
        assertThat(existUnits).isFalse();
    }

    @Test
    @DisplayName("isExistUnits 는 존재하는 유닛 ID일 경우 True를 리턴한다.")
    void isExistUnitsTrueTest() throws Exception {
        //given
        Augment testAugment1 = Augment.builder()
                .name("testAugmentName1")
                .season(season)
                .build();
        augmentRepository.save(testAugment1);
        Augment testAugment2 = Augment.builder()
                .name("testAugmentName2")
                .season(season)
                .build();
        augmentRepository.save(testAugment2);
        Augment testAugment3 = Augment.builder()
                .name("testAugmentName3")
                .season(season)
                .build();
        augmentRepository.save(testAugment3);
        //when
        List<Long> augmentIds = new ArrayList<>();
        augmentIds.add(testAugment1.getId());
        augmentIds.add(testAugment2.getId());
        augmentIds.add(testAugment3.getId());
        Boolean existUnits = augmentService.isExistUnits(augmentIds);
        //then
        assertThat(existUnits).isTrue();
    }
}