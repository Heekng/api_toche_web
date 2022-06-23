package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UnitServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    UnitService unitService;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    UnitRepository unitRepository;

    Season season;
    Unit unit;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        seasonRepository.save(season);

        unit = Unit.builder()
                .name("unitName")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(unit);
    }

    @Test
    @DisplayName("이미 존재하는 Unit 은 저장하지 않는다.")
    void findOrSaveFindTest() throws Exception {
        //when
        Unit findUnit = unitService.findOrSave(unit.getName(), unit.getRarity(), season);
        //then
        assertThat(findUnit.getId()).isEqualTo(unit.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Unit은 새로 저장한다.")
    void findOrSaveSaveTest() throws Exception {
        //when
        Unit findUnit = unitService.findOrSave("testUnitName", 1, season);
        em.flush();
        em.clear();
        Optional<Unit> optionalUnit = unitRepository.findById(findUnit.getId());
        //then
        assertThat(optionalUnit).isNotEmpty();
        assertThat(optionalUnit.get().getName()).isEqualTo(findUnit.getName());
        assertThat(optionalUnit.get().getSeason().getSeasonName()).isEqualTo(findUnit.getSeason().getSeasonName());
    }

    @Test
    @DisplayName("isExistUnits 는 존재하지 않는 유닛이 리스트로 넘어온 경우 false를 리턴한다.")
    void isExistUnitsFalseTest() throws Exception {
        //given
        Unit testUnit1 = Unit.builder()
                .name("testUnitName1")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(testUnit1);
        Unit testUnit2 = Unit.builder()
                .name("testUnitName2")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(testUnit2);
        //when
        List<Long> unitIds = new ArrayList<>();
        unitIds.add(testUnit1.getId());
        unitIds.add(testUnit2.getId());
        unitIds.add(1234L);
        Boolean existUnits = unitService.isExistUnits(unitIds);
        //then
        assertThat(existUnits).isFalse();
    }

    @Test
    @DisplayName("isExistUnits 는 존재하는 유닛이 리스트로 넘어온 경우 true를 리턴한다.")
    void isExistUnitsTrueTest() throws Exception {
        //given
        Unit testUnit1 = Unit.builder()
                .name("testUnitName1")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(testUnit1);
        Unit testUnit2 = Unit.builder()
                .name("testUnitName2")
                .season(season)
                .rarity(1)
                .build();
        unitRepository.save(testUnit2);
        //when
        List<Long> unitIds = new ArrayList<>();
        unitIds.add(testUnit1.getId());
        unitIds.add(testUnit2.getId());
        Boolean existUnits = unitService.isExistUnits(unitIds);
        //then
        assertThat(existUnits).isTrue();
    }

    @Test
    @DisplayName("findDetailByUnitId 는 unit 의 능력치, 스킬정보도 함께 조회한다.")
    void findDetailByUnitIdTest() throws Exception {
        //given
        Unit testUnit = Unit.builder()
                .name("newTestUnit")
                .krName("새로운 테스트 유닛")
                .rarity(0)
                .tier(null)
                .cost(1)
                .iconPath("aaaa")
                .season(season)
                .build();
        Ability testAbility = Ability.builder()
                .name("test skill")
                .krName("테스트 스킬")
                .abilityDesc("테스트 스킬 설명")
                .iconPath("bbbb")
                .build();
        testUnit.addAbility(testAbility);
        Stat testStat1 = Stat.builder()
                .name("testStat1")
                .statValue(1.1F)
                .build();
        testUnit.addStat(testStat1);
        Stat testStat2 = Stat.builder()
                .name("testStat2")
                .statValue(1.2F)
                .build();
        testUnit.addStat(testStat2);
        Stat testStat3 = Stat.builder()
                .name("testStat3")
                .statValue(1.3F)
                .build();
        testUnit.addStat(testStat3);
        unitRepository.save(testUnit);
        em.flush();
        em.clear();
        //when
        Unit findUnit = unitService.findDetailByUnitId(testUnit.getId());
        em.flush();
        em.clear();
        //then
        assertThat(findUnit).isNotNull();
        assertThat(findUnit.getAbilities().size()).isEqualTo(1);
        assertThat(findUnit.getAbilities().get(0).getName()).isEqualTo(testAbility.getName());
        assertThat(findUnit.getStats().size()).isEqualTo(3);
        assertThat(findUnit.getStats().get(0).getName()).isEqualTo(testStat1.getName());
        assertThat(findUnit.getStats().get(0).getStatValue()).isEqualTo(testStat1.getStatValue());
    }

    @Test
    @DisplayName("findDetailByUnitId 는 존재하지 않는 Unit 을 조회시 예외를 발생시킨다.")
    void findDetailByUnitIdExceptionTest() throws Exception {
        //given
        Unit testUnit = Unit.builder()
                .name("newTestUnit")
                .krName("새로운 테스트 유닛")
                .rarity(0)
                .tier(null)
                .cost(1)
                .iconPath("aaaa")
                .season(season)
                .build();
        Ability testAbility = Ability.builder()
                .name("test skill")
                .krName("테스트 스킬")
                .abilityDesc("테스트 스킬 설명")
                .iconPath("bbbb")
                .build();
        testUnit.addAbility(testAbility);
        Stat testStat1 = Stat.builder()
                .name("testStat1")
                .statValue(1.1F)
                .build();
        testUnit.addStat(testStat1);
        Stat testStat2 = Stat.builder()
                .name("testStat2")
                .statValue(1.2F)
                .build();
        testUnit.addStat(testStat2);
        Stat testStat3 = Stat.builder()
                .name("testStat3")
                .statValue(1.3F)
                .build();
        testUnit.addStat(testStat3);
        unitRepository.save(testUnit);
        em.flush();
        em.clear();
        //when
        assertThatThrownBy(() -> unitService.findDetailByUnitId(1234L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 Unit 입니다.");

    }

}