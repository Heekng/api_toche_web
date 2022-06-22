package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.*;
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

import java.time.LocalDateTime;
import java.util.List;
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
    }

    @Test
    @DisplayName("findByItemsRequest 는 itemsRequest 에 seasonId가 포함된 경우 해당 season 의 경기에 사용된 Item 을 리턴한다.")
    void findByItemsRequestTest() throws Exception {
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
        List<Item> findItems = itemService.findByItemsRequest(itemsRequest);
        //then
        assertThat(findItems).isNotEmpty();
        assertThat(findItems.get(0)).isEqualTo(testItem);
    }

    @Test
    @DisplayName("findByItemsRequest 는 itemsRequest 에 seasonId가 포함되지 않은 경우 검색 조건에 일치하는 Item 을 리턴한다.")
    void findByItemsRequestNonSeasonIdTest() throws Exception {
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
                .build();
        List<Item> findItems = itemService.findByItemsRequest(itemsRequest);
        //then
        assertThat(findItems).isNotEmpty();
        assertThat(findItems.size()).isEqualTo(2);
    }
}