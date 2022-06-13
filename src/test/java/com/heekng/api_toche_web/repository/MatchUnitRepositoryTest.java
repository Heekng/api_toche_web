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
class MatchUnitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchUnitRepository matchUnitRepository;

    Summoner summoner;
    TftMatch match;
    Season season;
    MatchInfo matchInfo;
    Unit unit;
    MatchUnit matchUnit;

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
                .seasonNum(6)
                .build();
        em.persist(season);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        matchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(match)
                .season(season)
                .build();
        em.persist(matchInfo);

        unit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(season)
                .cost(5)
                .build();
        em.persist(unit);

        matchUnit = MatchUnit.builder()
                .unit(unit)
                .matchInfo(matchInfo)
                .build();
        matchUnitRepository.save(matchUnit);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchUnit> findByIdObject = matchUnitRepository.findById(matchUnit.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchUnit);

        // findAll
        List<MatchUnit> findAllObject = matchUnitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchUnitRepository.delete(matchUnit);
        Optional<MatchUnit> afterDeleteObject = matchUnitRepository.findById(matchUnit.getId());
        assertThat(afterDeleteObject).isEmpty();
    }
}