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
import java.util.ArrayList;
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

    @Test
    void searchWithFromItemByItemIdTest() throws Exception {
        //given
        Item testItemFrom1 = Item.builder()
                .num(2)
                .name("testItemFrom1")
                .krName("테스트 아이템 1")
                .iconPath("testItemFrom1.png")
                .build();
        itemRepository.save(testItemFrom1);
        Item testItemFrom2 = Item.builder()
                .num(3)
                .name("testItemFrom2")
                .krName("테스트 아이템 2")
                .iconPath("testItemFrom2.png")
                .build();
        itemRepository.save(testItemFrom2);
        Item testItemBase = Item.builder()
                .num(4)
                .name("testItemBaseName")
                .krName("테스트 아이템 베이스")
                .fromItem1(2)
                .fromItem2(3)
                .iconPath("testItemBase.png")
                .build();
        itemRepository.save(testItemBase);
        //when
        Optional<ItemDTO.ItemDetailResponse> itemDetailResponse = itemRepository.searchWithFromItemByItemId(testItemBase.getId());
        //then
        System.out.println(itemDetailResponse.toString());
    }

    @Test
    void searchItemRankByUnitIdTest() throws Exception {
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
                .seasonName("testSeason")
                .build();
        em.persist(testSeason);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        MatchInfo testMatchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(testTftMatch)
                .season(testSeason)
                .build();
        em.persist(testMatchInfo);

        Item testItem = Item.builder()
                .name("testItemName")
                .krName("테스트 아이템 이름")
                .num(1)
                .build();
        em.persist(testItem);

        Item testItem2 = Item.builder()
                .name("testItemName2")
                .krName("테스트 아이템 이름2")
                .num(2)
                .build();
        em.persist(testItem2);

        MatchItem matchItem1 = MatchItem.builder()
                .item(testItem)
                .build();

        MatchItem matchItem2 = MatchItem.builder()
                .item(testItem)
                .build();

        MatchItem matchItem3 = MatchItem.builder()
                .item(testItem2)
                .build();

        Unit testUnit1 = Unit.builder()
                .rarity(1)
                .name("testUnit1")
                .tier(1)
                .season(testSeason)
                .cost(5)
                .build();
        em.persist(testUnit1);

        Unit testUnit2 = Unit.builder()
                .rarity(1)
                .name("testUnit2")
                .tier(1)
                .season(testSeason)
                .cost(5)
                .build();
        em.persist(testUnit2);

        MatchUnit testMatchUnit1 = MatchUnit.builder()
                .unit(testUnit1)
                .matchInfo(testMatchInfo)
                .build();
        testMatchUnit1.addMatchItem(matchItem1);
        testMatchUnit1.addMatchItem(matchItem3);
        em.persist(testMatchUnit1);

        MatchUnit testMatchUnit2 = MatchUnit.builder()
                .unit(testUnit2)
                .matchInfo(testMatchInfo)
                .build();
        testMatchUnit2.addMatchItem(matchItem2);
        em.persist(testMatchUnit2);
        //when
        List<ItemDTO.UnitRankResponse> unitRankResponses = itemRepository.searchItemRankByUnitId(testUnit1.getId());
        //then
        assertThat(unitRankResponses).isNotEmpty();
        assertThat(unitRankResponses.size()).isEqualTo(2);
        assertThat(unitRankResponses.get(0).getChampionUsedCount()).isEqualTo(1);
    }

    @Test
    void searchByNumsTest() throws Exception {
        //given
        Item testItem1 = Item.builder()
                .name("testItem1")
                .num(2)
                .build();
        itemRepository.save(testItem1);
        Item testItem2 = Item.builder()
                .name("testItem2")
                .num(3)
                .build();
        itemRepository.save(testItem2);
        Item testItem3 = Item.builder()
                .name("testItem3")
                .num(4)
                .build();
        itemRepository.save(testItem3);
        //when
        List<Integer> itemNums = List.of(2, 3, 5);
        List<Item> items = itemRepository.searchByNums(itemNums);
        //then
        assertThat(items).isNotEmpty();
        assertThat(items.size()).isEqualTo(2);

    }

    @Test
    void searchSeasonUsedItemBySeasonIdTest() throws Exception {
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
        List<Item> findItems = itemRepository.searchSeasonUsedItemBySeasonId(testSeason.getId());
        //then
        assertThat(findItems).isNotEmpty();
        assertThat(findItems.get(0)).isEqualTo(testItem);
    }
}