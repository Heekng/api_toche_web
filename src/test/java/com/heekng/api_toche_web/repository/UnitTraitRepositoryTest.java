package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.enums.TraitStyle;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitTraitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UnitTraitRepository unitTraitRepository;

    Season season;
    Trait trait;
    Unit unit;
    UnitTrait unitTrait;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum("testSeasonNum")
                .build();
        em.persist(season);

        trait = Trait.builder()
                .season(season)
                .name("traitName")
                .tierTotalCount(5)
                .build();
        em.persist(trait);

        unit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(season)
                .cost(5)
                .build();
        em.persist(unit);

        unitTrait = UnitTrait.builder()
                .unit(unit)
                .trait(trait)
                .build();
        unitTraitRepository.save(unitTrait);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<UnitTrait> findByIdObject = unitTraitRepository.findById(unitTrait.getUnitTraitId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(unitTrait);

        // findAll
        List<UnitTrait> findAllObject = unitTraitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        unitTraitRepository.delete(unitTrait);
        Optional<UnitTrait> afterDeleteObject = unitTraitRepository.findById(unitTrait.getUnitTraitId());
        assertThat(afterDeleteObject).isEmpty();
    }
}