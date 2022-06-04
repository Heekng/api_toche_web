package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchInfo;
import com.heekng.api_toche_web.entity.Season;
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
class SeasonRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    SeasonRepository seasonRepository;

    Season season;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum("testSeason")
                .build();
        seasonRepository.save(season);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Season> findByIdObject = seasonRepository.findById(season.getSeasonId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(season);

        // findAll
        List<Season> findAllObject = seasonRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        seasonRepository.delete(season);
        Optional<Season> afterDeleteObject = seasonRepository.findById(season.getSeasonId());
        assertThat(afterDeleteObject).isEmpty();
    }

}