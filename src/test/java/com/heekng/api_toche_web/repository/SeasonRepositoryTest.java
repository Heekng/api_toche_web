package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.SeasonDTO;
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
                .seasonName("TFTSet6")
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

    @Test
    void searchBySeasonsRequestTest() throws Exception {
        //given
        Season testSeason1 = Season.builder()
                .seasonNum(5)
                .seasonName("testSeason1")
                .build();
        seasonRepository.save(testSeason1);
        Season testSeason2 = Season.builder()
                .seasonNum(5)
                .seasonName("testSeason2")
                .build();
        seasonRepository.save(testSeason2);
        Season testSeason2_2 = Season.builder()
                .seasonNum(6)
                .seasonName("testSeason2_2")
                .build();
        seasonRepository.save(testSeason2_2);
        //when
        SeasonDTO.SeasonsRequest seasonsRequest = SeasonDTO.SeasonsRequest.builder()
                .seasonNum(5)
                .build();
        List<Season> seasons = seasonRepository.searchBySeasonsRequest(seasonsRequest);
        //then
        assertThat(seasons).isNotEmpty();
        assertThat(seasons.size()).isEqualTo(2);
        assertThat(seasons.get(0).getSeasonName()).isEqualTo(testSeason2.getSeasonName());
    }

    @Test
    void findBySeasonNameTest() throws Exception {
        //when
        Optional<Season> seasonOptional = seasonRepository.findBySeasonName(season.getSeasonName());
        //then
        assertThat(seasonOptional).isNotEmpty();
        assertThat(seasonOptional.get()).isEqualTo(season);
    }
}