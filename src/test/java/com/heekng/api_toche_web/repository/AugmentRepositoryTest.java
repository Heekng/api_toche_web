package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
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
class AugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    AugmentRepository augmentRepository;

    Augment augment;
    Season season;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        augment = Augment.builder()
                .name("augmentName")
                .season(season)
                .build();
        augmentRepository.save(augment);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Augment> findByIdObject = augmentRepository.findById(augment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(augment);

        // findAll
        List<Augment> findAllObject = augmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        augmentRepository.delete(augment);
        Optional<Augment> afterDeleteObject = augmentRepository.findById(augment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}