package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        Optional<Item> optionalItem = itemRepository.findByName(item.getName());
        //then
        assertThat(optionalItem).isNotEmpty();
        assertThat(optionalItem.get().getName()).isEqualTo(item.getName());
        assertThat(optionalItem.get().getNum()).isEqualTo(item.getNum());
    }

    @Test
    void searchByRiotItemIdTest() throws Exception {
        //when
        Optional<Item> itemOptional = itemRepository.searchItemByRiotItemId(item.getNum());
        //then
        assertThat(itemOptional).isNotEmpty();
        assertThat(itemOptional.get()).isEqualTo(item);
    }

    @Test
    @DisplayName("searchByItemsRequest 는 itemNum를 이용할 수 있으며, itemNum 오름차순으로 정렬된다.")
    void searchByItemsRequestTest() throws Exception {
        Item testItem = Item.builder()
                .name("testItemName")
                .num(1)
                .build();
        em.persist(testItem);
        //when
        ItemDTO.ItemsRequest itemsRequest = ItemDTO.ItemsRequest.builder()
                .itemNum(testItem.getNum())
                .build();
        List<Item> items = itemRepository.searchByItemsRequest(itemsRequest);
        //then
        assertThat(items).isNotEmpty();
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(0).getName()).isEqualTo(item.getName());
    }

    @Test
    void findByNumTest() throws Exception {
        //when
        Optional<Item> itemOptional = itemRepository.findByNum(item.getNum());
        //then
        assertThat(itemOptional).isNotEmpty();
        assertThat(itemOptional.get()).isEqualTo(item);
    }
}