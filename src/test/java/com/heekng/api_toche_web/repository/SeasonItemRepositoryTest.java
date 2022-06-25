package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
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
public class SeasonItemRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    SeasonItemRepository seasonItemRepository;

    Season season;
    Item item;
    SeasonItem seasonItem;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(1)
                .seasonName("seasonName")
                .build();
        em.persist(season);

        item = Item.builder()
                .num(1)
                .name("itemName")
                .build();
        em.persist(item);

        seasonItem = SeasonItem.builder()
                .item(item)
                .season(season)
                .build();
        seasonItemRepository.save(seasonItem);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<SeasonItem> findByIdObject = seasonItemRepository.findById(seasonItem.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(seasonItem);

        // findAll
        List<SeasonItem> findAllObject = seasonItemRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        seasonItemRepository.delete(seasonItem);
        Optional<SeasonItem> afterDeleteObject = seasonItemRepository.findById(seasonItem.getId());
        assertThat(afterDeleteObject).isEmpty();
    }
}
