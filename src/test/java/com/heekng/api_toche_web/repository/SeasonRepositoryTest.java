package com.heekng.api_toche_web.repository;

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
                .seasonNum(6)
                .build();
        seasonRepository.save(season);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Season> findByIdObject = seasonRepository.findById(season.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(season);

        // findAll
        List<Season> findAllObject = seasonRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        seasonRepository.delete(season);
        Optional<Season> afterDeleteObject = seasonRepository.findById(season.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void findBySeasonNumTest() throws Exception {
        //when
        Optional<Season> findSeason = seasonRepository.findBySeasonNum(6);
        //then
        assertThat(findSeason).isNotEmpty();
        assertThat(findSeason.get()).isEqualTo(season);

    }

}