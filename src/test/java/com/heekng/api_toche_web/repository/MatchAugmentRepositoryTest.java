package com.heekng.api_toche_web.repository;

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

@SpringBootTest
@Transactional
class MatchAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchAugmentRepository matchAugmentRepository;

    Summoner summoner;
    TftMatch tftMatch;
    MatchInfo matchInfo;
    Augment augment;
    Season season;
    MatchAugment matchAugment;

    @BeforeEach
    void beforeEach() {
        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        tftMatch = TftMatch.builder()
                .matchId("1234567")
                .summoner(summoner)
                .build();
        em.persist(tftMatch);

        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        matchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(tftMatch)
                .season(season)
                .build();
        em.persist(matchInfo);

        augment = Augment.builder()
                .name("augmentName")
                .season(season)
                .build();
        em.persist(augment);

        matchAugment = MatchAugment.builder()
                .augment(augment)
                .matchInfo(matchInfo)
                .build();
        matchAugmentRepository.save(matchAugment);

    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchAugment> findByIdObject = matchAugmentRepository.findById(matchAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchAugment);

        // findAll
        List<MatchAugment> findAllObject = matchAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchAugmentRepository.delete(matchAugment);
        Optional<MatchAugment> afterDeleteObject = matchAugmentRepository.findById(matchAugment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}