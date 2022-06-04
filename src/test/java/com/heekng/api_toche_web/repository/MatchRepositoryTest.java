package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Challenger;
import com.heekng.api_toche_web.entity.Match;
import com.heekng.api_toche_web.entity.Summoner;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MatchRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchRepository matchRepository;

    Summoner summoner;
    Match match;

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
        matchRepository.save(match);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Match> findByIdObject = matchRepository.findById(match.getMatchId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(match);

        // findAll
        List<Match> findAllObject = matchRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchRepository.delete(match);
        Optional<Match> afterDeleteObject = matchRepository.findById(match.getMatchId());
        assertThat(afterDeleteObject).isEmpty();
    }
}