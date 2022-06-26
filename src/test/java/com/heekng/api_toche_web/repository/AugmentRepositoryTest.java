package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    AugmentRepository augmentRepository;

    Augment augment;
    Season season;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        augment = Augment.builder()
                .name("augmentName")
                .build();
        augmentRepository.save(augment);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Augment> findByIdObject = augmentRepository.findById(augment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(augment);

        // findAll
        List<Augment> findAllObject = augmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        augmentRepository.delete(augment);
        Optional<Augment> afterDeleteObject = augmentRepository.findById(augment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void findByNameTest() throws Exception {
        //when
        Augment findAugment = augmentRepository.findByName(augment.getName()).get();
        //then
        assertThat(findAugment).isEqualTo(augment);
    }

    @Test
    void searchByAugmentsRequestTest() throws Exception {
        //given
        Augment testAugment = Augment.builder()
                .name("testAugmentName")
                .build();
        augmentRepository.save(testAugment);
        //when
        AugmentDTO.AugmentsRequest augmentsRequest = AugmentDTO.AugmentsRequest.builder()
                .seasonId(season.getId())
                .build();
        List<Augment> augments = augmentRepository.searchByAugmentsRequest(augmentsRequest);
        //then
        assertThat(augments).isNotEmpty();
        assertThat(augments.size()).isEqualTo(2);
        assertThat(augments.get(0)).isEqualTo(augment);
    }

    @Test
    void findByNumTest() throws Exception {
        //when
        Optional<Augment> augmentOptional = augmentRepository.findByNum(augment.getNum());
        //then
        assertThat(augmentOptional).isNotEmpty();
        assertThat(augmentOptional.get()).isEqualTo(augment);
    }

    @Test
    void findByNameEndingWithTest() throws Exception {
        //when
        Optional<Augment> augmentOptional = augmentRepository.findByNameEndingWith("mentName");
        //then
        assertThat(augmentOptional).isNotEmpty();
        assertThat(augmentOptional.get()).isEqualTo(augment);

    }

    @Test
    void searchSeasonUsedAugmentBySeasonIdTest() throws Exception {
        //given
        Summoner testSummoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(testSummoner);

        TftMatch testTftMatch = TftMatch.builder()
                .matchId("1234567")
                .summoner(testSummoner)
                .build();
        em.persist(testTftMatch);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        MatchInfo testMatchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(testTftMatch)
                .season(season)
                .build();
        em.persist(testMatchInfo);

        Augment testAugment1 = Augment.builder()
                .name("testAugment1")
                .build();
        augmentRepository.save(testAugment1);

        Augment testAugment2 = Augment.builder()
                .name("testAugment2")
                .build();
        augmentRepository.save(testAugment2);

        MatchAugment matchAugment1 = MatchAugment.builder()
                .augment(testAugment1)
                .matchInfo(testMatchInfo)
                .build();
        em.persist(matchAugment1);

        MatchAugment matchAugment2 = MatchAugment.builder()
                .augment(testAugment2)
                .matchInfo(testMatchInfo)
                .build();
        em.persist(matchAugment2);

        //when
        List<Augment> augments = augmentRepository.searchSeasonUsedAugmentBySeasonId(season.getId());
        //then
        assertThat(augments).isNotEmpty();
        assertThat(augments.size()).isEqualTo(2);
        assertThat(augments).contains(testAugment1, testAugment2);
    }

    @Test
    void searchByAugmentsRequestContainsSeasonIdTest() throws Exception {
        //given
        Season testSeason1 = Season.builder()
                .seasonNum(1)
                .seasonName("testSeason1")
                .build();
        em.persist(testSeason1);
        Season testSeason2 = Season.builder()
                .seasonNum(2)
                .seasonName("testSeason2")
                .build();
        em.persist(testSeason2);

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
        Augment testAugment4 = Augment.builder()
                .name("testAugment4")
                .build();
        em.persist(testAugment4);

        SeasonAugment testSeasonAugment1 = SeasonAugment.builder()
                .season(testSeason1)
                .augment(testAugment1)
                .build();
        em.persist(testSeasonAugment1);
        SeasonAugment testSeasonAugment2 = SeasonAugment.builder()
                .season(testSeason1)
                .augment(testAugment2)
                .build();
        em.persist(testSeasonAugment2);
        SeasonAugment testSeasonAugment3 = SeasonAugment.builder()
                .season(testSeason1)
                .augment(testAugment3)
                .build();
        em.persist(testSeasonAugment3);
        SeasonAugment testSeasonAugment4 = SeasonAugment.builder()
                .season(testSeason2)
                .augment(testAugment4)
                .build();
        em.persist(testSeasonAugment4);
        //when
        AugmentDTO.AugmentsRequest augmentsRequest = AugmentDTO.AugmentsRequest.builder()
                .seasonId(testSeason1.getId())
                .augmentName("testAugment")
                .build();
        List<Augment> findAugments = augmentRepository.searchByAugmentsRequestContainsSeasonId(augmentsRequest);
        //then
        assertThat(findAugments).isNotEmpty();
        assertThat(findAugments.size()).isEqualTo(3);
        assertThat(findAugments).contains(testAugment1, testAugment2, testAugment3);
    }

}