package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UseDeckAugmentServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckAugmentService useDeckAugmentService;

    @Test
    void findOrSaveByAugmentsAndSeasonTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonName("testSeason")
                .seasonNum(7)
                .build();
        em.persist(season);
        Augment testAugment1 = Augment.builder()
                .name("testAugment1")
                .build();
        em.persist(testAugment1);
        Augment testAugment2 = Augment.builder()
                .name("testAugment2")
                .build();
        em.persist(testAugment2);
        Augment testAugment3 = Augment.builder()
                .name("testAugment3")
                .build();
        em.persist(testAugment3);
        //when
        List<Augment> augments = List.of(testAugment1, testAugment2, testAugment3);
        UseDeckAugment useDeckAugment = useDeckAugmentService.findOrSaveByAugmentsAndSeason(augments, season);
        List<Augment> findAugments = useDeckAugment.getUseAugments().stream()
                .map(UseAugment::getAugment)
                .collect(Collectors.toList());
        //then
        assertThat(findAugments).contains(testAugment1);
        assertThat(findAugments).contains(testAugment2);
        assertThat(findAugments).contains(testAugment3);
    }
}