package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AugmentRepository extends JpaRepository<Augment, Long>, AugmentRepositoryCustom {

    Optional<Augment> findByName(String name);

    Optional<Augment> findByNameAndSeasonId(String name, Long SeasonId);

    @EntityGraph(attributePaths = "season")
    Optional<Augment> findWithSeasonById(Long id);
}
