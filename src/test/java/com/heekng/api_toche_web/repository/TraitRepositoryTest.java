package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.TraitSet;
import com.heekng.api_toche_web.enums.TraitStyle;
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

@SpringBootTest
@Transactional
class TraitRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    TraitRepository traitRepository;

    Season season;
    Trait trait;

    @BeforeEach
    void beforeEach() {
        season = Season.builder()
                .seasonNum(6)
                .seasonName("TFTSet6")
                .build();
        em.persist(season);

        trait = Trait.builder()
                .season(season)
                .name("traitName")
                .tierTotalCount(5)
                .build();
        traitRepository.save(trait);
    }

    @Test
    void basicTest() throws Exception {
        // findById
        Optional<Trait> findByIdObject = traitRepository.findById(trait.getId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(trait);

        // findAll
        List<Trait> findAllObject = traitRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        // delete
        traitRepository.delete(trait);
        Optional<Trait> afterDeleteObject = traitRepository.findById(trait.getId());
        assertThat(afterDeleteObject).isEmpty();
    }

    @Test
    void cascadePersistTest() throws Exception {
        //given
        Season seasonCascade = Season.builder()
                .seasonNum(7)
                .seasonName("TFTSet7")
                .build();
        em.persist(seasonCascade);

        Trait traitCascade = Trait.builder()
                .season(season)
                .name("traitNameCascade")
                .tierTotalCount(5)
                .build();

        TraitSet traitSetCascade = TraitSet.builder()
                .min(1)
                .max(3)
                .style(TraitStyle.valueOf("_" + 1))
                .trait(traitCascade)
                .build();

        traitCascade.addTraitSets(List.of(traitSetCascade));

        //when
        traitRepository.save(traitCascade);
        em.flush();
        em.clear();
        //then
        Trait findTrait = traitRepository.findById(traitCascade.getId()).get();
        TraitSet traitSet = findTrait.getTraitSets().get(0);
        assertThat(findTrait).isNotNull();
        assertThat(traitSet).isNotNull();
    }

    @Test
    void findBySeasonSeasonIdAndNameContainingTest() throws Exception {
        //when
        Trait findTrait = traitRepository.findBySeasonIdAndNameContaining(season.getId(), trait.getName());
        //then
        assertThat(findTrait).isNotNull();
        assertThat(findTrait).isEqualTo(trait);

    }

    @Test
    void findByNameAndSeasonIdTest() throws Exception {
        //when
        Optional<Trait> traitOptional = traitRepository.findByNameAndSeasonId(trait.getName(), season.getId());
        //then
        assertThat(traitOptional).isNotEmpty();
        assertThat(traitOptional.get().getName()).isEqualTo(trait.getName());
        assertThat(traitOptional.get().getTierTotalCount()).isEqualTo(trait.getTierTotalCount());
        assertThat(traitOptional.get().getSeason().getSeasonName()).isEqualTo(season.getSeasonName());
    }
}