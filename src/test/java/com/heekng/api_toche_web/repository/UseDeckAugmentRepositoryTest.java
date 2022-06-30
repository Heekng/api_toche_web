package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UnitTrait;
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

@SpringBootTest
@Transactional
class UseDeckAugmentRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckAugmentRepository useDeckAugmentRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);

        //when
        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(season)
                .useCount(0L)
                .build();
        //then
        //save
        useDeckAugmentRepository.save(useDeckAugment);
        // findById
        Optional<UseDeckAugment> findByIdObject = useDeckAugmentRepository.findById(useDeckAugment.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeckAugment);

        // findAll
        List<UseDeckAugment> findAllObject = useDeckAugmentRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckAugmentRepository.delete(useDeckAugment);
        Optional<UseDeckAugment> afterDeleteObject = useDeckAugmentRepository.findById(useDeckAugment.getId());
        assertThat(afterDeleteObject).isEmpty();

    }
}