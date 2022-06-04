package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import org.junit.jupiter.api.BeforeEach;
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
class MatchItemRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchItemRepository matchItemRepository;

    Summoner summoner;
    Match match;
    Season season;
    MatchInfo matchInfo;
    Unit unit;
    MatchUnit matchUnit;
    Item item;
    MatchItem matchItem;

    @BeforeEach
    void beforeEach() {
        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        match = Match.builder()
                .victoryMatchId("1234567")
                .summoner(summoner)
                .build();
        em.persist(match);

        season = Season.builder()
                .seasonNum("testSeasonNum")
                .build();
        em.persist(season);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        matchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .match(match)
                .season(season)
                .build();
        em.persist(matchInfo);

        unit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(season)
                .build();
        em.persist(unit);

        matchUnit = MatchUnit.builder()
                .unit(unit)
                .matchInfo(matchInfo)
                .build();
        em.persist(matchUnit);

        item = Item.builder()
                .name("itemName")
                .num(1)
                .season(season)
                .build();
        em.persist(item);

        matchItem = MatchItem.builder()
                .item(item)
                .matchUnit(matchUnit)
                .build();
        matchItemRepository.save(matchItem);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchItem> findByIdObject = matchItemRepository.findById(matchItem.getMatchItemId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchItem);

        // findAll
        List<MatchItem> findAllObject = matchItemRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchItemRepository.delete(matchItem);
        Optional<MatchItem> afterDeleteObject = matchItemRepository.findById(matchItem.getMatchItemId());
        assertThat(afterDeleteObject).isEmpty();
    }

}