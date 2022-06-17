package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class GuidServiceTest {

    @Autowired
    GuidService guidService;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    MatchInfoRepository matchInfoRepository;
    @Autowired
    SummonerRepository summonerRepository;
    @Autowired
    TftMatchRepository tftMatchRepository;

    @Test
    @DisplayName("guidByUnits 는 입력한 유닛 ID가 포함된 승리 수가 가장 많은 덱을 제공한다.")
    void guidByUnitsTest() throws Exception {
        //given
        Season testSeason = Season.builder()
                .seasonName("testSeason")
                .seasonNum(1)
                .build();
        seasonRepository.save(testSeason);
        Unit testUnit1 = Unit.builder()
                .name("testUnit1")
                .season(testSeason)
                .build();
        unitRepository.save(testUnit1);
        Unit testUnit2 = Unit.builder()
                .name("testUnit2")
                .season(testSeason)
                .build();
        unitRepository.save(testUnit2);
        Unit testUnit3 = Unit.builder()
                .name("testUnit3")
                .season(testSeason)
                .build();
        unitRepository.save(testUnit3);
        Summoner testSummoner = Summoner.builder()
                .id("summonerId")
                .name("summonerName")
                .puuid("123-123-123")
                .build();
        summonerRepository.save(testSummoner);

        TftMatch testTftMatch = TftMatch.builder()
                .matchId("1234567")
                .summoner(testSummoner)
                .build();
        tftMatchRepository.save(testTftMatch);
        LocalDateTime localDateTime = LocalDateTime.of(2022, 6, 17, 18, 32);
        MatchInfo testMatchInfo = MatchInfo.builder()
                .gameDatetime(localDateTime)
                .ranking(1)
                .season(testSeason)
                .tftMatch(testTftMatch)
                .build();
        MatchUnit matchUnit1 = MatchUnit.builder()
                .unit(testUnit1)
                .build();
        MatchUnit matchUnit2 = MatchUnit.builder()
                .unit(testUnit2)
                .build();
        MatchUnit matchUnit3 = MatchUnit.builder()
                .unit(testUnit3)
                .build();
        testMatchInfo.addMatchUnit(matchUnit1);
        testMatchInfo.addMatchUnit(matchUnit2);
        testMatchInfo.addMatchUnit(matchUnit3);
        matchInfoRepository.save(testMatchInfo);
        //when
        List<Long> unitIds = new ArrayList<>();
        unitIds.add(testUnit1.getId());
        unitIds.add(testUnit2.getId());
        unitIds.add(testUnit3.getId());
        UnitDTO.GuidRequest guidRequest = UnitDTO.GuidRequest.builder()
                .unitIds(unitIds)
                .build();
        UnitDTO.GuidResultResponse guidResultResponse = guidService.guidByUnits(guidRequest);
        //then
        Assertions.assertThat(guidResultResponse.getResultCount()).isEqualTo(1);
        Assertions.assertThat(guidResultResponse.getAllUsedCount()).isEqualTo(1);
        Assertions.assertThat(guidResultResponse.getUnits().size()).isEqualTo(3);
    }
}