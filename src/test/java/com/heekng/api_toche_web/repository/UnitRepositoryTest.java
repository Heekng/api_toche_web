package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
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
class UnitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UnitRepository unitRepository;

    Season season;
    Unit unit;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum("testSeasonNum")
                .build();
        em.persist(season);

        unit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(season)
                .cost(5)
                .build();
        unitRepository.save(unit);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Unit> findByIdObject = unitRepository.findById(unit.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(unit);

        // findAll
        List<Unit> findAllObject = unitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        unitRepository.delete(unit);
        Optional<Unit> afterDeleteObject = unitRepository.findById(unit.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}