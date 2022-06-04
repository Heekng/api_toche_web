package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Match;
import com.heekng.api_toche_web.entity.MatchInfo;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Summoner;
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
class MatchInfoRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchInfoRepository matchInfoRepository;

    Summoner summoner;
    Match match;
    Season season;
    MatchInfo matchInfo;

    @BeforeEach
    void beforeEach() {
        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        match = Match.builder()
                .victoryMatchId("1234567")
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
                .match(match)
                .season(season)
                .build();
        matchInfoRepository.save(matchInfo);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchInfo> findByIdObject = matchInfoRepository.findById(matchInfo.getMatchInfoId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchInfo);

        // findAll
        List<MatchInfo> findAllObject = matchInfoRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchInfoRepository.delete(matchInfo);
        Optional<MatchInfo> afterDeleteObject = matchInfoRepository.findById(matchInfo.getMatchInfoId());
        assertThat(afterDeleteObject).isEmpty();
    }
}