package com.heekng.api_toche_web.repository;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@SpringBootTest
@Transactional
class MatchInfoRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MatchInfoRepository matchInfoRepository;

    Summoner summoner;
    TftMatch match;
    Season season;
    MatchInfo matchInfo;

    @BeforeEach
    void beforeEach() {
        summoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        em.persist(summoner);

        match = TftMatch.builder()
                .matchId("1234567")
                .summoner(summoner)
                .build();
        em.persist(match);

        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        LocalDateTime gameDatetime = LocalDateTime.of(2022, 6, 4, 15, 22);
        matchInfo = MatchInfo.builder()
                .gameDatetime(gameDatetime)
                .tftMatch(match)
                .season(season)
                .build();
        matchInfoRepository.save(matchInfo);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<MatchInfo> findByIdObject = matchInfoRepository.findById(matchInfo.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(matchInfo);

        // findAll
        List<MatchInfo> findAllObject = matchInfoRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        matchInfoRepository.delete(matchInfo);
        Optional<MatchInfo> afterDeleteObject = matchInfoRepository.findById(matchInfo.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    @DisplayName("searchByUnitContains 는 요청한 유닛 ID가 포함된 경기 기록을 리턴한다.")
    void searchByUnitContainsTest() throws Exception {
        //given
        Unit testUnit1 = Unit.builder()
                .season(season)
                .name("testUnit1")
                .build();
        em.persist(testUnit1);
        Unit testUnit2 = Unit.builder()
                .season(season)
                .name("testUnit2")
                .build();
        em.persist(testUnit2);
        Unit testUnit3 = Unit.builder()
                .season(season)
                .name("testUnit3")
                .build();
        em.persist(testUnit3);
        MatchUnit matchUnit1 = MatchUnit.builder()
                .unit(testUnit1)
                .matchInfo(matchInfo)
                .build();
        em.persist(matchUnit1);
        MatchUnit matchUnit2 = MatchUnit.builder()
                .unit(testUnit2)
                .matchInfo(matchInfo)
                .build();
        em.persist(matchUnit2);
        MatchUnit matchUnit3 = MatchUnit.builder()
                .unit(testUnit3)
                .matchInfo(matchInfo)
                .build();
        em.persist(matchUnit3);

        LocalDateTime matchDateTime = LocalDateTime.of(2022, 6, 16, 17, 48);
        MatchInfo testMatchInfo = MatchInfo.builder()
                .tftMatch(match)
                .season(season)
                .ranking(2)
                .gameDatetime(matchDateTime)
                .build();
        MatchUnit matchUnit4 = MatchUnit.builder()
                .unit(testUnit1)
                .matchInfo(testMatchInfo)
                .build();
        testMatchInfo.addMatchUnit(matchUnit4);
        em.persist(testMatchInfo);
        em.flush();
        em.clear();
        //when
        List<MatchInfo> matchInfos = matchInfoRepository.searchByUnitContains(Arrays.asList(testUnit1.getId(), testUnit2.getId()));

        //then
        assertThat(matchInfos).isNotEmpty();
        assertThat(matchInfos.size()).isEqualTo(1);
        assertThat(matchInfos.get(0).getId()).isEqualTo(matchInfo.getId());
    }

    @Test
    void listEqTest() throws Exception {
        List<Long> testList1 = new ArrayList<>();
        testList1.add(1L);
        testList1.add(2L);
        testList1.add(3L);
        List<Long> testList2 = new ArrayList<>();
        testList2.add(1L);
        testList2.add(2L);
        testList2.add(3L);
        assertThat(testList1).isEqualTo(testList2);
    }
}