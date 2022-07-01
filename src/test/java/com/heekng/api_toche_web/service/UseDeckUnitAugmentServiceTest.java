package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseDeckAugment;
import com.heekng.api_toche_web.entity.UseDeckUnit;
import com.heekng.api_toche_web.entity.UseDeckUnitAugment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UseDeckUnitAugmentServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckUnitAugmentService useDeckUnitAugmentService;

    @Test
    void findOrSaveByUseDeckUnitAndUseDeckAugmentTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);
        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .useCount(0L)
                .build();
        em.persist(useDeckUnit);
        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        em.persist(useDeckAugment);
        //when
        UseDeckUnitAugment useDeckUnitAugment = useDeckUnitAugmentService.findOrSaveByUseDeckUnitAndUseDeckAugment(useDeckUnit, useDeckAugment);
        //then
        assertThat(useDeckUnitAugment.getUseDeckAugment()).isEqualTo(useDeckAugment);
        assertThat(useDeckUnitAugment.getUseDeckUnit()).isEqualTo(useDeckUnit);
    }

}