package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
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

    @Test
    void searchByItemsRequestContainsSeasonIdTest() throws Exception {
        //given
        Summoner testSummoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(testSummoner);

        TftMatch testTftMatch = TftMatch.builder()
                .matchId("1234567")
                .summoner(testSummoner)
                .build();
        em.persist(testTftMatch);

        Season testSeason = Season.builder()
                .seasonNum(6)
                .seasonName("testSeasonName")
                .build();
        em.persist(testSeason);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        MatchInfo testMatchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(testTftMatch)
                .season(testSeason)
                .build();
        em.persist(testMatchInfo);

        Unit testUnit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(testSeason)
                .cost(5)
                .build();
        em.persist(testUnit);

        MatchUnit testMatchUnit = MatchUnit.builder()
                .unit(testUnit)
                .matchInfo(testMatchInfo)
                .build();
        em.persist(testMatchUnit);

        Item testItem = Item.builder()
                .name("testItemName")
                .num(2)
                .build();
        em.persist(testItem);

        MatchItem testMatchItem = MatchItem.builder()
                .item(testItem)
                .matchUnit(testMatchUnit)
                .build();
        em.persist(testMatchItem);
        //when
        ItemDTO.ItemsRequest itemsRequest = ItemDTO.ItemsRequest.builder()
                .seasonId(testSeason.getId())
                .build();
        List<Item> findItems = itemRepository.searchByItemsRequestContainsSeasonId(itemsRequest);
        //then
        assertThat(findItems).isNotEmpty();
        assertThat(findItems.get(0)).isEqualTo(testItem);
    }
}