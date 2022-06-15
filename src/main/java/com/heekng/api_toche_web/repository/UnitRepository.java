package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Unit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long>, UnitRepositoryCustom {

    Optional<Unit> findByNameAndSeasonId(String name, Long seasonId);

    @EntityGraph(attributePaths = "season")
    Optional<Unit> findWithSeasonById(Long id);
}
