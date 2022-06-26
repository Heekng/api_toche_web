package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.SeasonAugment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonAugmentRepository extends JpaRepository<SeasonAugment, Long>, SeasonAugmentRepositoryCustom {

    Boolean existsBySeasonIdAndAugmentId(Long seasonId, Long augmentId);
}
