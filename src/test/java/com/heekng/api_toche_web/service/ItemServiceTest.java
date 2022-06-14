package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.SeasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemService itemService;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    ItemRepository itemRepository;

    Season season;
    Item item;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        seasonRepository.save(season);

        item = Item.builder()
                .season(season)
                .num(1)
                .name("itemName")
                .build();
        itemRepository.save(item);
    }

    @Test
    @DisplayName("이미 존재하는 Item 은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Item findItem = itemService.findOrSave(item.getName(), item.getNum(), season);
        //then
        assertThat(findItem.getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Item 은 새로 저장한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Item findItem = itemService.findOrSave("testItemName", 2, season);
        em.flush();
        em.clear();
        Optional<Item> itemOptional = itemRepository.findById(findItem.getId());
        //then
        assertThat(itemOptional).isNotEmpty();
        assertThat(itemOptional.get().getName()).isEqualTo(findItem.getName());
        assertThat(itemOptional.get().getSeason().getSeasonName()).isEqualTo(findItem.getSeason().getSeasonName());
    }

}