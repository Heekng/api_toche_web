package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Challenger;
import com.heekng.api_toche_web.entity.ChallengerInquiry;
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
class ChallengerRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    ChallengerRepository challengerRepository;

    ChallengerInquiry challengerInquiry;
    Summoner summoner;
    Challenger challenger;


    @BeforeEach
    void beforeEach() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, 6, 3, 21, 41);
        challengerInquiry = ChallengerInquiry.builder()
                .inquiryDatetime(localDateTime)
                .build();
        em.persist(challengerInquiry);

        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        challenger = Challenger.builder()
                .challengerInquiry(challengerInquiry)
                .summoner(summoner)
                .build();
        challengerRepository.save(challenger);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Challenger> findByIdObject = challengerRepository.findById(challenger.getChallengerId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(challenger);

        // findAll
        List<Challenger> findAllObject = challengerRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        challengerRepository.delete(challenger);
        Optional<Challenger> afterDeleteObject = challengerRepository.findById(challenger.getChallengerId());
        assertThat(afterDeleteObject).isEmpty();
    }

}