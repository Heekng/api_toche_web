package com.heekng.api_toche_web.repository;

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

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemRepository itemRepository;

    Item item;
    Season season;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        item = Item.builder()
                .name("itemName")
                .num(1)
                .season(season)
                .build();
        itemRepository.save(item);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Item> findByIdObject = itemRepository.findById(item.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(item);

        // findAll
        List<Item> findAllObject = itemRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        itemRepository.delete(item);
        Optional<Item> afterDeleteObject = itemRepository.findById(item.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void findByNameAndSeasonIdTest() throws Exception {
        //when
        Optional<Item> optionalItem = itemRepository.findByNameAndSeasonId(item.getName(), season.getId());
        //then
        assertThat(optionalItem).isNotEmpty();
        assertThat(optionalItem.get().getName()).isEqualTo(item.getName());
        assertThat(optionalItem.get().getNum()).isEqualTo(item.getNum());
        assertThat(optionalItem.get().getSeason().getSeasonName()).isEqualTo(season.getSeasonName());
    }
}