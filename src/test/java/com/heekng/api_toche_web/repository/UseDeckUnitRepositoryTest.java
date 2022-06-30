package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
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
        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        em.persist(useDeckAugment);
        UseDeck useDeck = UseDeck.builder()
                .useDeckAugment(useDeckAugment)
                .useCount(0L)
                .build();
        em.persist(useDeck);
        Unit unit = Unit.builder()
                .name("testUnit")
                .season(season)
                .build();
        em.persist(unit);
        //when
        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .unit(unit)
                .useDeck(useDeck)
                .build();
        //then
        //save
        useDeckUnitRepository.save(useDeckUnit);
        // findById
        Optional<UseDeckUnit> findByIdObject = useDeckUnitRepository.findById(useDeckUnit.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeckUnit);

        // findAll
        List<UseDeckUnit> findAllObject = useDeckUnitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckUnitRepository.delete(useDeckUnit);
        Optional<UseDeckUnit> afterDeleteObject = useDeckUnitRepository.findById(useDeckUnit.getId());
        assertThat(afterDeleteObject).isEmpty();
    }
}