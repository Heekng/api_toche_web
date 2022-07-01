package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseDeckUnit;
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
class UseDeckRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UseDeckUnitRepository useDeckRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Season season = Season.builder()
                .seasonNum(7)
                .seasonName("testSeason")
                .build();
        em.persist(season);
        //when
        UseDeckUnit useDeck = UseDeckUnit.builder()
                .useCount(0L)
                .build();
        //then
        //save
        useDeckRepository.save(useDeck);
        // findById
        Optional<UseDeckUnit> findByIdObject = useDeckRepository.findById(useDeck.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(useDeck);

        // findAll
        List<UseDeckUnit> findAllObject = useDeckRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        useDeckRepository.delete(useDeck);
        Optional<UseDeckUnit> afterDeleteObject = useDeckRepository.findById(useDeck.getId());
        assertThat(afterDeleteObject).isEmpty();

    }
}