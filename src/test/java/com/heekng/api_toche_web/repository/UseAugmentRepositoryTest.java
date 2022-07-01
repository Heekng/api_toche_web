package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseAugment;
import com.heekng.api_toche_web.entity.UseDeckAugment;
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
class UseAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseAugmentRepository useAugmentRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);
        Augment augment = Augment.builder()
                .name("testAugment")
                .build();
        em.persist(augment);
        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .useCount(0L)
                .season(season)
                .build();
        em.persist(useDeckAugment);
        //when
        UseAugment useAugment = UseAugment.builder()
                .augment(augment)
                .useDeckAugment(useDeckAugment)
                .build();
        //then
        //save
        useAugmentRepository.save(useAugment);
        // findById
        Optional<UseAugment> findByIdObject = useAugmentRepository.findById(useAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useAugment);

        // findAll
        List<UseAugment> findAllObject = useAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useAugmentRepository.delete(useAugment);
        Optional<UseAugment> afterDeleteObject = useAugmentRepository.findById(useAugment.getId());
        assertThat(afterDeleteObject).isEmpty();
    }
}