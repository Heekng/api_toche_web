package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.entity.UseDeckUnit;
import com.heekng.api_toche_web.entity.UseUnit;
import org.assertj.core.api.Assertions;
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
class UseDeckUnitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckUnitRepository useDeckUnitRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);
        //when
        UseDeckUnit useDeck = UseDeckUnit.builder()
                .useCount(0L)
                .build();
        //then
        //save
        useDeckUnitRepository.save(useDeck);
        // findById
        Optional<UseDeckUnit> findByIdObject = useDeckUnitRepository.findById(useDeck.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeck);

        // findAll
        List<UseDeckUnit> findAllObject = useDeckUnitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckUnitRepository.delete(useDeck);
        Optional<UseDeckUnit> afterDeleteObject = useDeckUnitRepository.findById(useDeck.getId());
        assertThat(afterDeleteObject).isEmpty();

    }
    
    @Test
    void searchByUnitsTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);

        Unit testUnit1 = Unit.builder()
                .name("testUnit1")
                .season(season)
                .build();
        em.persist(testUnit1);
        Unit testUnit2 = Unit.builder()
                .name("testUnit2")
                .season(season)
                .build();
        em.persist(testUnit2);
        Unit testUnit3 = Unit.builder()
                .name("testUnit3")
                .season(season)
                .build();
        em.persist(testUnit3);
        Unit testUnit4 = Unit.builder()
                .name("testUnit4")
                .season(season)
                .build();
        em.persist(testUnit4);

        UseUnit useUnit1 = UseUnit.builder()
                .unit(testUnit1)
                .build();
        UseUnit useUnit2 = UseUnit.builder()
                .unit(testUnit2)
                .build();
        UseUnit useUnit3 = UseUnit.builder()
                .unit(testUnit3)
                .build();
        UseUnit useUnit4 = UseUnit.builder()
                .unit(testUnit4)
                .build();

        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .useCount(0L)
                .build();
        useDeckUnit.insertUseUnit(useUnit1);
        useDeckUnit.insertUseUnit(useUnit2);
        useDeckUnit.insertUseUnit(useUnit3);
        useDeckUnit.insertUseUnit(useUnit4);
        useDeckUnitRepository.save(useDeckUnit);
        //when
        List<Unit> units = List.of(testUnit1, testUnit2, testUnit3, testUnit4);
        Optional<UseDeckUnit> useDeckUnitOptional = useDeckUnitRepository.searchByUnits(units);
        //then
        assertThat(useDeckUnitOptional).isNotEmpty();
        assertThat(useDeckUnitOptional.get()).isEqualTo(useDeckUnit);
    }
}