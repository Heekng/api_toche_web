package com.heekng.api_toche_web.service;

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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UseDeckUnitServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckUnitService useDeckUnitService;

    @Test
    void findOrSaveByUnitsTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonName("testSeason")
                .seasonNum(7)
                .build();
        em.persist(season);
        Unit testUnit1 = Unit.builder()
                .season(season)
                .name("testUnit1")
                .build();
        em.persist(testUnit1);
        Unit testUnit2 = Unit.builder()
                .season(season)
                .name("testUnit2")
                .build();
        em.persist(testUnit2);
        Unit testUnit3 = Unit.builder()
                .season(season)
                .name("testUnit3")
                .build();
        em.persist(testUnit3);
        //when
        List<Unit> units = List.of(testUnit1, testUnit2, testUnit3);
        UseDeckUnit useDeckUnit = useDeckUnitService.findOrSaveByUnits(units);
        List<Unit> findUnits = useDeckUnit.getUseUnits().stream()
                .map(UseUnit::getUnit)
                .collect(Collectors.toList());
        //then
        assertThat(findUnits).contains(testUnit1);
        assertThat(findUnits).contains(testUnit2);
        assertThat(findUnits).contains(testUnit3);
    }
}