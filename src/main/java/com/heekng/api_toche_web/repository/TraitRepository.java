package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Trait;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraitRepository extends JpaRepository<Trait, Long>, TraitRepositoryCustom {

    Trait findBySeasonIdAndNameContaining(Long seasonId, String name);

    Optional<Trait> findByNameAndSeasonId(String name, Long seasonId);

    @EntityGraph(attributePaths = "season")
    Optional<Trait> findWithSeasonById(Long id);
}
