package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Stat;
import com.heekng.api_toche_web.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StatRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    StatRepository statRepository;
    @Autowired
    UnitRepository unitRepository;

    Season season;
    Unit unit;
    Stat stat1;
    Stat stat2;

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
        em.persist(unit);

        stat1 = Stat.builder()
                .name("statName1")
                .statValue(1.0F)
                .unit(unit)
                .build();
        statRepository.save(stat1);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Stat> findByIdObject = statRepository.findById(stat1.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(stat1);

        // findAll
        List<Stat> findAllObject = statRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        statRepository.delete(stat1);
        Optional<Stat> afterDeleteObject = statRepository.findById(stat1.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void mergeTest() throws Exception {
        //given
        Season testSeason = Season.builder()
                .seasonNum(7)
                .seasonName("TFTSet7")
                .build();
        em.persist(testSeason);

        Stat testStat1 = Stat.builder()
                .name("statName1")
                .statValue(1.0F)
                .build();

        Unit testUnit = Unit.builder()
                .rarity(1)
                .name("mergeTestUnit")
                .tier(1)
                .season(testSeason)
                .cost(5)
                .build();
        em.persist(testUnit);
        //when
        testUnit.addStat(testStat1);
        em.flush();
        em.clear();

        Unit findTestUnit = unitRepository.findById(testUnit.getId()).get();
        //then
        assertThat(findTestUnit.getStats().size()).isEqualTo(1);
        assertThat(findTestUnit.getStats().get(0).getUnit().getId()).isEqualTo(testUnit.getId());
    }
}