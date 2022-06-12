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
class MatchTraitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchTraitRepository matchTraitRepository;

    Summoner summoner;
    TftMatch match;
    Season season;
    MatchInfo matchInfo;
    Trait trait;
    MatchTrait matchTrait;

    @BeforeEach
    void beforeEach() {
        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        match = TftMatch.builder()
                .matchId("1234567")
                .summoner(summoner)
                .build();
        em.persist(match);

        season = Season.builder()
                .seasonNum("testSeasonNum")
                .build();
        em.persist(season);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        matchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(match)
                .season(season)
                .build();
        em.persist(matchInfo);

        trait = Trait.builder()
                .season(season)
                .name("traitName")
                .tierTotalCount(5)
                .build();
        em.persist(trait);

        matchTrait = MatchTrait.builder()
                .matchInfo(matchInfo)
                .trait(trait)
                .styleNum(1)
                .tierAppliedCount(2)
                .unitCount(3)
                .build();
        matchTraitRepository.save(matchTrait);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchTrait> findByIdObject = matchTraitRepository.findById(matchTrait.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchTrait);

        // findAll
        List<MatchTrait> findAllObject = matchTraitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchTraitRepository.delete(matchTrait);
        Optional<MatchTrait> afterDeleteObject = matchTraitRepository.findById(matchTrait.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}