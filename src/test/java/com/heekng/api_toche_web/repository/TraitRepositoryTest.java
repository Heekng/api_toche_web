package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
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
class TraitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    TraitRepository traitRepository;

    Season season;
    Trait trait;

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
        traitRepository.save(trait);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Trait> findByIdObject = traitRepository.findById(trait.getTraitsId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(trait);

        // findAll
        List<Trait> findAllObject = traitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        traitRepository.delete(trait);
        Optional<Trait> afterDeleteObject = traitRepository.findById(trait.getTraitsId());
        assertThat(afterDeleteObject).isEmpty();
    }
}