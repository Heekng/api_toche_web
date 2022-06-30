package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseDeck;
import com.heekng.api_toche_web.entity.UseDeckAugment;
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
class UseDeckRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckRepository useDeckRepository;

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
        //when
        UseDeck useDeck = UseDeck.builder()
                .useDeckAugment(useDeckAugment)
                .useCount(0L)
                .build();
        //then
        //save
        useDeckRepository.save(useDeck);
        // findById
        Optional<UseDeck> findByIdObject = useDeckRepository.findById(useDeck.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeck);

        // findAll
        List<UseDeck> findAllObject = useDeckRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckRepository.delete(useDeck);
        Optional<UseDeck> afterDeleteObject = useDeckRepository.findById(useDeck.getId());
        assertThat(afterDeleteObject).isEmpty();

    }
}