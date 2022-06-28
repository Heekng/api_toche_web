package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.TraitDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.entity.UnitTrait;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.TraitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TraitServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    TraitRepository traitRepository;
    @Autowired
    TraitService traitService;
    @Autowired
    SeasonRepository seasonRepository;

    Season season;
    Trait trait;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        seasonRepository.save(season);

        trait = Trait.builder()
                .name("traitName")
                .tierTotalCount(4)
                .season(season)
                .build();
        traitRepository.save(trait);
    }

    @Test
    @DisplayName("이미 존재하는 Trait 은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Trait findTrait = traitService.findOrSave(trait.getName(), trait.getTierTotalCount(), season);
        //then
        assertThat(findTrait.getId()).isEqualTo(trait.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Trait 은 새로 저장한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Trait findTrait = traitService.findOrSave("testTraitName", 3, season);
        em.flush();
        em.clear();
        Optional<Trait> traitOptional = traitRepository.findById(findTrait.getId());
        //then
        assertThat(traitOptional).isNotEmpty();
        assertThat(traitOptional.get().getName()).isEqualTo(findTrait.getName());
        assertThat(traitOptional.get().getSeason().getSeasonName()).isEqualTo(findTrait.getSeason().getSeasonName());
    }

    @Test
    void findTraitDetailTest() throws Exception {
        //given
        Season testSeason10 = Season.builder()
                .seasonNum(10)
                .seasonName("testSeason10")
                .build();
        em.persist(testSeason10);
        Unit testUnit1 = Unit.builder()
                .name("testUnit1")
                .season(testSeason10)
                .build();
        em.persist(testUnit1);
        Unit testUnit2 = Unit.builder()
                .name("testUnit2")
                .season(testSeason10)
                .build();
        em.persist(testUnit2);
        Trait testTrait1 = Trait.builder()
                .name("testTrait1")
                .tierTotalCount(5)
                .season(testSeason10)
                .build();
        em.persist(testTrait1);
        UnitTrait unitTrait1 = UnitTrait.builder()
                .trait(testTrait1)
                .unit(testUnit1)
                .build();
        em.persist(unitTrait1);
        UnitTrait unitTrait2 = UnitTrait.builder()
                .trait(testTrait1)
                .unit(testUnit2)
                .build();
        em.persist(unitTrait2);
        //when
        TraitDTO.TraitDetailResponse traitDetailResponse = traitService.findTraitDetail(testTrait1.getId());
        //then
        assertThat(traitDetailResponse).isNotNull();
        assertThat(traitDetailResponse.getName()).isEqualTo(testTrait1.getName());
        assertThat(traitDetailResponse.getUnits().size()).isEqualTo(2);

    }

}