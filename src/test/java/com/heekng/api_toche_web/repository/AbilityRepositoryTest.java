package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Ability;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AbilityRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    AbilityRepository abilityRepository;

    Season season;
    Unit unit;
    Ability ability;

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

        ability = Ability.builder()
                .name("testAbility")
                .desc("testAbilityDesc")
                .krName("테스트 능력")
                .iconPath("abcd")
                .unit(unit)
                .build();
        abilityRepository.save(ability);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Ability> findByIdObject = abilityRepository.findById(ability.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(ability);

        // findAll
        List<Ability> findAllObject = abilityRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        abilityRepository.delete(ability);
        Optional<Ability> afterDeleteObject = abilityRepository.findById(ability.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

}