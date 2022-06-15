package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
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
class UnitRepositoryTest {

    @PersistenceContext
    EntityManager em;
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
        em.persist(season);

        unit = Unit.builder()
                .rarity(1)
                .name("testUnit")
                .tier(1)
                .season(season)
                .cost(5)
                .build();
        unitRepository.save(unit);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Unit> findByIdObject = unitRepository.findById(unit.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(unit);

        // findAll
        List<Unit> findAllObject = unitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        unitRepository.delete(unit);
        Optional<Unit> afterDeleteObject = unitRepository.findById(unit.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void findByNameAndSeasonIdTest() throws Exception {
        //when
        Optional<Unit> findUnitOptional = unitRepository.findByNameAndSeasonId(unit.getName(), season.getId());
        //then
        assertThat(findUnitOptional).isNotEmpty();
        assertThat(findUnitOptional.get().getName()).isEqualTo(unit.getName());
        assertThat(findUnitOptional.get().getSeason().getSeasonName()).isEqualTo(season.getSeasonName());
    }

    @Test
    @DisplayName("findWithSeasonById 는 season 을 fetch join 한다.")
    void findWithSeasonById() throws Exception {
        //when
        Optional<Unit> optionalUnit = unitRepository.findWithSeasonById(unit.getId());
        em.flush();
        em.clear();
        //then
        assertThat(optionalUnit).isNotEmpty();
        assertThat(optionalUnit.get().getId()).isEqualTo(unit.getId());
        assertThat(optionalUnit.get().getSeason()).isNotNull();
        assertThat(optionalUnit.get().getSeason().getSeasonName()).isEqualTo(season.getSeasonName());
    }

    @Test
    void searchByUnitsRequestTest() throws Exception {
        //given
        Season testSeason = Season.builder()
                .seasonNum(7)
                .seasonName("TFTSet7")
                .build();
        em.persist(testSeason);
        Unit testUnit1 = Unit.builder()
                .rarity(1)
                .name("testUnit1")
                .tier(1)
                .season(season)
                .cost(5)
                .build();
        unitRepository.save(testUnit1);
        Unit testUnit2 = Unit.builder()
                .rarity(1)
                .name("testUnit2")
                .tier(1)
                .season(testSeason)
                .cost(5)
                .build();
        unitRepository.save(testUnit2);
        //when
        UnitDTO.UnitsRequest unitsRequest = UnitDTO.UnitsRequest.builder()
                .seasonId(season.getId())
                .build();
        List<Unit> units = unitRepository.searchByUnitsRequest(unitsRequest);

        //then
        assertThat(units).isNotEmpty();
        assertThat(units.size()).isEqualTo(2);
        assertThat(units.get(0).getName()).isEqualTo("testUnit");
    }
}