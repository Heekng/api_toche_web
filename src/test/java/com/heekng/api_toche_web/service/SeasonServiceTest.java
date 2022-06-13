package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.SeasonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SeasonServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    SeasonService seasonService;
    @Autowired
    SeasonRepository seasonRepository;

    Season season;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);
    }

    @Test
    @DisplayName("이미 존재하는 Season은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Season findSeason = seasonService.findOrSave(6, "TFTSet6");
        //then
        assertThat(season.getId()).isEqualTo(findSeason.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Season은 새로 저장하여 리턴한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Season findSeason = seasonService.findOrSave(7, "TFTSet7");
        em.flush();
        em.clear();
        Optional<Season> seasonOptional = seasonRepository.findById(findSeason.getId());
        //then
        assertThat(seasonOptional).isNotEmpty();
        assertThat(seasonOptional.get().getSeasonName()).isEqualTo(findSeason.getSeasonName());
        assertThat(seasonOptional.get().getSeasonName()).isEqualTo(findSeason.getSeasonName());
    }

}