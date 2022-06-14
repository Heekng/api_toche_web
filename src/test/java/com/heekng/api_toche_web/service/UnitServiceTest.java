package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UnitService unitService;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    UnitRepository unitRepository;

    Season season;
    Unit unit;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        seasonRepository.save(season);

        unit = Unit.builder()
                .name("unitName")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(unit);
    }

    @Test
    @DisplayName("이미 존재하는 Unit 은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Unit findUnit = unitService.findOrSave(unit.getName(), unit.getRarity(), season);
        //then
        assertThat(findUnit.getId()).isEqualTo(unit.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Unit은 새로 저장한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Unit findUnit = unitService.findOrSave("testUnitName", 1, season);
        em.flush();
        em.clear();
        Optional<Unit> optionalUnit = unitRepository.findById(findUnit.getId());
        //then
        assertThat(optionalUnit).isNotEmpty();
        assertThat(optionalUnit.get().getName()).isEqualTo(findUnit.getName());
        assertThat(optionalUnit.get().getSeason().getSeasonName()).isEqualTo(findUnit.getSeason().getSeasonName());
    }



}