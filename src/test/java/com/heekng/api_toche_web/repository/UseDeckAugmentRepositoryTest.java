package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import org.junit.jupiter.api.BeforeEach;
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
class UseDeckAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckAugmentRepository useDeckAugmentRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);

        //when
        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        //then
        //save
        useDeckAugmentRepository.save(useDeckAugment);
        // findById
        Optional<UseDeckAugment> findByIdObject = useDeckAugmentRepository.findById(useDeckAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeckAugment);

        // findAll
        List<UseDeckAugment> findAllObject = useDeckAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckAugmentRepository.delete(useDeckAugment);
        Optional<UseDeckAugment> afterDeleteObject = useDeckAugmentRepository.findById(useDeckAugment.getId());
        assertThat(afterDeleteObject).isEmpty();

    }

    @Test
    void searchByAugmentsAndSeasonTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
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

        UseAugment useAugment1 = UseAugment.builder()
                .augment(testAugment1)
                .build();
        UseAugment useAugment2 = UseAugment.builder()
                .augment(testAugment2)
                .build();
        UseAugment useAugment3 = UseAugment.builder()
                .augment(testAugment3)
                .build();

        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        useDeckAugment.insertUseAugment(useAugment1);
        useDeckAugment.insertUseAugment(useAugment2);
        useDeckAugment.insertUseAugment(useAugment3);
        useDeckAugmentRepository.save(useDeckAugment);
        //when
        List<Augment> augments = List.of(testAugment1, testAugment2, testAugment3);
        Optional<UseDeckAugment> useDeckAugmentOptional = useDeckAugmentRepository.searchByAugmentsAndSeason(augments, season);
        //then
        assertThat(useDeckAugmentOptional).isNotEmpty();
        assertThat(useDeckAugmentOptional.get()).isEqualTo(useDeckAugment);
    }

    @Test
    void searchAugmentContainsByAugmentIdsAndSeasonIdTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
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

        UseAugment useAugment1 = UseAugment.builder()
                .augment(testAugment1)
                .build();
        UseAugment useAugment2 = UseAugment.builder()
                .augment(testAugment2)
                .build();
        UseAugment useAugment3 = UseAugment.builder()
                .augment(testAugment3)
                .build();

        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        useDeckAugment.insertUseAugment(useAugment1);
        useDeckAugment.insertUseAugment(useAugment2);
        useDeckAugment.insertUseAugment(useAugment3);
        useDeckAugmentRepository.save(useDeckAugment);
        //when
        List<Long> augmentIds = List.of(testAugment1.getId(), testAugment2.getId());
        List<UseDeckAugment> useDeckAugments = useDeckAugmentRepository.searchAugmentContainsByAugmentIdsAndSeasonId(augmentIds, season.getId());
        //then
        assertThat(useDeckAugments).isNotEmpty();
        assertThat(useDeckAugments.size()).isEqualTo(1);
        assertThat(useDeckAugments.get(0)).isEqualTo(useDeckAugment);
    }
}