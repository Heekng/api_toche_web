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

@SpringBootTest
@Transactional
class UseUnitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseUnitRepository useUnitRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);
        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .useCount(0L)
                .build();
        em.persist(useDeckUnit);
        Unit unit = Unit.builder()
                .name("testUnit")
                .season(season)
                .build();
        em.persist(unit);
        //when
        UseUnit useUnit = UseUnit.builder()
                .unit(unit)
                .useDeckUnit(useDeckUnit)
                .build();
        //then
        //save
        useUnitRepository.save(useUnit);
        // findById
        Optional<UseUnit> findByIdObject = useUnitRepository.findById(useUnit.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useUnit);

        // findAll
        List<UseUnit> findAllObject = useUnitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useUnitRepository.delete(useUnit);
        Optional<UseUnit> afterDeleteObject = useUnitRepository.findById(useUnit.getId());
        assertThat(afterDeleteObject).isEmpty();
    }
}