package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.TraitSet;
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

@SpringBootTest
@Transactional
class TraitSetRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    TraitSetRepository traitSetRepository;

    Season season;
    Trait trait;
    TraitSet traitSet;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .build();
        em.persist(season);

        trait = Trait.builder()
                .season(season)
                .name("traitName")
                .tierTotalCount(5)
                .build();
        em.persist(trait);

        traitSet = TraitSet.builder()
                .trait(trait)
                .min(1)
                .max(3)
                .style(TraitStyle.valueOf("_" + 1))
                .build();
        traitSetRepository.save(traitSet);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<TraitSet> findByIdObject = traitSetRepository.findById(traitSet.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(traitSet);

        // findAll
        List<TraitSet> findAllObject = traitSetRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        traitSetRepository.delete(traitSet);
        Optional<TraitSet> afterDeleteObject = traitSetRepository.findById(traitSet.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}