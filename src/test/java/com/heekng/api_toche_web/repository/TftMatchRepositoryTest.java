package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.TftMatch;
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

@SpringBootTest
@Transactional
class TftMatchRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    TftMatchRepository matchRepository;

    Summoner summoner;
    TftMatch match;

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
        matchRepository.save(match);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<TftMatch> findByIdObject = matchRepository.findById(match.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(match);

        // findAll
        List<TftMatch> findAllObject = matchRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchRepository.delete(match);
        Optional<TftMatch> afterDeleteObject = matchRepository.findById(match.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void existsByMatchIdTest() throws Exception {
        //when
        Boolean existsByMatchId = matchRepository.existsByMatchId(match.getMatchId());
        Boolean notExistsByMatchId = matchRepository.existsByMatchId("123123");
        //then
        assertThat(existsByMatchId).isTrue();
        assertThat(notExistsByMatchId).isFalse();
    }
}