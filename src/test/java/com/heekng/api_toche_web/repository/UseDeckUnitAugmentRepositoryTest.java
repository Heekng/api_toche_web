package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UseDeckUnitAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckUnitAugmentRepository useDeckUnitAugmentRepository;

    @Test
    void basicTest() throws Exception {
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
                .useCount(0L)
                .season(season)
                .build();
        em.persist(useDeckAugment);
        //when
        UseDeckUnitAugment useDeckUnitAugment = UseDeckUnitAugment.builder()
                .useDeckUnit(useDeckUnit)
                .useDeckAugment(useDeckAugment)
                .useCount(0L)
                .build();
        //then
        //save
        useDeckUnitAugmentRepository.save(useDeckUnitAugment);
        // findById
        Optional<UseDeckUnitAugment> findByIdObject = useDeckUnitAugmentRepository.findById(useDeckUnitAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeckUnitAugment);

        // findAll
        List<UseDeckUnitAugment> findAllObject = useDeckUnitAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckUnitAugmentRepository.delete(useDeckUnitAugment);
        Optional<UseDeckUnitAugment> afterDeleteObject = useDeckUnitAugmentRepository.findById(useDeckUnitAugment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void findByUseDeckUnitAndUseDeckAugmentTest() throws Exception {
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
                .useCount(0L)
                .season(season)
                .build();
        em.persist(useDeckAugment);
        UseDeckUnitAugment useDeckUnitAugment = UseDeckUnitAugment.builder()
                .useDeckUnit(useDeckUnit)
                .useDeckAugment(useDeckAugment)
                .useCount(0L)
                .build();
        useDeckUnitAugmentRepository.save(useDeckUnitAugment);
        //when
        Optional<UseDeckUnitAugment> useDeckUnitAugmentOptional = useDeckUnitAugmentRepository.findByUseDeckUnitAndUseDeckAugment(useDeckUnit, useDeckAugment);
        //then
        assertThat(useDeckUnitAugmentOptional).isNotEmpty();
        assertThat(useDeckUnitAugmentOptional.get()).isEqualTo(useDeckUnitAugment);
    }

    @Test
    void findByUseDeckAugmentTest() throws Exception {
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
                .useCount(0L)
                .season(season)
                .build();
        em.persist(useDeckAugment);
        UseDeckUnitAugment useDeckUnitAugment = UseDeckUnitAugment.builder()
                .useDeckUnit(useDeckUnit)
                .useDeckAugment(useDeckAugment)
                .useCount(0L)
                .build();
        useDeckUnitAugmentRepository.save(useDeckUnitAugment);
        //when
        List<UseDeckUnitAugment> useDeckUnitAugments = useDeckUnitAugmentRepository.findByUseDeckAugment(useDeckAugment);
        //then
        assertThat(useDeckUnitAugments).isNotEmpty();
        assertThat(useDeckUnitAugments.size()).isEqualTo(1);
        assertThat(useDeckUnitAugments.get(0)).isEqualTo(useDeckUnitAugment);

    }
}