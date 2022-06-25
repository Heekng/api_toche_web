package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.SeasonAugment;
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
class SeasonAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    SeasonAugmentRepository seasonAugmentRepository;

    Season season;
    Augment augment;
    SeasonAugment seasonAugment;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(1)
                .seasonName("seasonName")
                .build();
        em.persist(season);

        augment = Augment.builder()
                .name("augmentName")
                .build();
        em.persist(augment);

        seasonAugment = SeasonAugment.builder()
                .augment(augment)
                .season(season)
                .build();
        seasonAugmentRepository.save(seasonAugment);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<SeasonAugment> findByIdObject = seasonAugmentRepository.findById(seasonAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(seasonAugment);

        // findAll
        List<SeasonAugment> findAllObject = seasonAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        seasonAugmentRepository.delete(seasonAugment);
        Optional<SeasonAugment> afterDeleteObject = seasonAugmentRepository.findById(seasonAugment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}