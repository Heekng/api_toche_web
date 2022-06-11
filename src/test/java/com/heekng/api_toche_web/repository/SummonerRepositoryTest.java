package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Summoner;
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
class SummonerRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    SummonerRepository summonerRepository;

    @Test
    void basicTest() throws Exception {
        Summoner summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();

        // save
        summonerRepository.save(summoner);

        // findById
        Optional<Summoner> findByIdObject = summonerRepository.findById(summoner.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(summoner);

        // findAll
        List<Summoner> findAllObject = summonerRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        summonerRepository.delete(summoner);
        Optional<Summoner> afterDeleteObject = summonerRepository.findById(summoner.getId());
        assertThat(afterDeleteObject).isEmpty();

    }
}