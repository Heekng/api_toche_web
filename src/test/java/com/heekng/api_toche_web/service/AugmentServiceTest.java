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

}