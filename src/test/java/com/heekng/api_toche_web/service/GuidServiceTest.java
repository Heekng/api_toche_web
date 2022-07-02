package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.dto.GuidDTO;
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

import static org.assertj.core.api.Assertions.*;

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
    @Autowired
    AugmentRepository augmentRepository;
    @Autowired
    UseDeckUnitRepository useDeckUnitRepository;
    @Autowired
    UseDeckAugmentRepository useDeckAugmentRepository;
    @Autowired
    UseDeckUnitAugmentRepository useDeckUnitAugmentRepository;

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
        UseUnit useUnit1 = UseUnit.builder()
                .unit(testUnit1)
                .build();
        UseUnit useUnit2 = UseUnit.builder()
                .unit(testUnit2)
                .build();
        UseUnit useUnit3 = UseUnit.builder()
                .unit(testUnit3)
                .build();
        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .useCount(1L)
                .build();
        useDeckUnit.insertUseUnit(useUnit1);
        useDeckUnit.insertUseUnit(useUnit2);
        useDeckUnit.insertUseUnit(useUnit3);
        useDeckUnitRepository.save(useDeckUnit);
        //when
        List<Long> unitIds = List.of(testUnit1.getId(), testUnit2.getId());
        UnitDTO.GuidRequest guidRequest = UnitDTO.GuidRequest.builder()
                .unitIds(unitIds)
                .build();
        GuidDTO.GuidResultResponse guidResultResponse = guidService.guidByUnits(guidRequest);
        //then
        assertThat(guidResultResponse).isNotNull();
        assertThat(guidResultResponse.getResultCount()).isEqualTo(1);
        assertThat(guidResultResponse.getAllUsedCount()).isEqualTo(1);
        assertThat(guidResultResponse.getUnits().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("guidByAugments 는 입력한 증강체 ID가 포함된 승리 수가 가장 많은 덱을 제공한다.")
    void guidByAugmentsTest() throws Exception {
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
        UseUnit useUnit1 = UseUnit.builder()
                .unit(testUnit1)
                .build();
        UseUnit useUnit2 = UseUnit.builder()
                .unit(testUnit2)
                .build();
        UseUnit useUnit3 = UseUnit.builder()
                .unit(testUnit3)
                .build();
        UseDeckUnit useDeckUnit = UseDeckUnit.builder()
                .useCount(1L)
                .build();
        useDeckUnit.insertUseUnit(useUnit1);
        useDeckUnit.insertUseUnit(useUnit2);
        useDeckUnit.insertUseUnit(useUnit3);
        useDeckUnitRepository.save(useDeckUnit);

        Augment testAugment1 = Augment.builder()
                .name("testAugment1")
                .build();
        augmentRepository.save(testAugment1);
        System.out.println("id1:" + testAugment1.getId());
        Augment testAugment2 = Augment.builder()
                .name("testAugment2")
                .build();
        augmentRepository.save(testAugment2);
        System.out.println("id2:" + testAugment2.getId());
        Augment testAugment3 = Augment.builder()
                .name("testAugment3")
                .build();
        augmentRepository.save(testAugment3);
        System.out.println("id3:" + testAugment3.getId());

        UseAugment useAugment1 = UseAugment.builder()
                .augment(testAugment1)
                .build();
        UseAugment useAugment2 = UseAugment.builder()
                .augment(testAugment2)
                .build();
        UseAugment useAugment3 = UseAugment.builder()
                .augment(testAugment3)
                .build();

        UseDeckAugment useDeckAugment = UseDeckAugment.builder()
                .season(testSeason)
                .useCount(1L)
                .build();
        useDeckAugment.insertUseAugment(useAugment1);
        useDeckAugment.insertUseAugment(useAugment2);
        useDeckAugment.insertUseAugment(useAugment3);
        useDeckAugmentRepository.save(useDeckAugment);

        UseDeckUnitAugment useDeckUnitAugment = UseDeckUnitAugment.builder()
                .useDeckAugment(useDeckAugment)
                .useCount(1L)
                .useDeckUnit(useDeckUnit)
                .build();
        useDeckUnitAugmentRepository.save(useDeckUnitAugment);
        //when
        List<Long> augmentIds = List.of(testAugment1.getId(), testAugment2.getId());
        AugmentDTO.GuidRequest guidRequest = AugmentDTO.GuidRequest.builder()
                .augmentIds(augmentIds)
                .seasonId(testSeason.getId())
                .build();
        GuidDTO.GuidResultResponse guidResultResponse = guidService.guidByAugments(guidRequest);
        //then
        assertThat(guidResultResponse).isNotNull();
        assertThat(guidResultResponse.getResultCount()).isEqualTo(1);
        assertThat(guidResultResponse.getAllUsedCount()).isEqualTo(1);
        assertThat(guidResultResponse.getUnits().size()).isEqualTo(3);
    }


}