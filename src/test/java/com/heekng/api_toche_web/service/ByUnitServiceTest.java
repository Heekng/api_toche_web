package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class ByUnitServiceTest {

    @Autowired
    GuidService guidService;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    UnitRepository unitRepository;

    @Test
    @DisplayName("존재하지 않는 유닛 ID가 포함되면 IllegalArgumentException 가 발생한다.")
    void guidByUnitsNotExistUnitIdTest() throws Exception {
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
        //when
        UnitDTO.GuidRequest guidRequest = UnitDTO.GuidRequest.builder()
                .unitIds(
                        Arrays.asList(testUnit1.getId(), testUnit2.getId(), 100L)
                )
                .build();
        //then
        Assertions.assertThatThrownBy(() -> guidService.guidByUnits(guidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guidByUnits() throws Exception {
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
        //when
        UnitDTO.GuidRequest guidRequest = UnitDTO.GuidRequest.builder()
                .unitIds(
                        Arrays.asList(testUnit1.getId(), testUnit2.getId(), testUnit3.getId())
                )
                .build();
        guidService.guidByUnits(guidRequest);
        //then
    }
}