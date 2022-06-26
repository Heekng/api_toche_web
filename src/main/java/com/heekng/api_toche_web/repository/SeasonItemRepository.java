package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.SeasonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonItemRepository extends JpaRepository<SeasonItem, Long>, SeasonItemRepositoryCustom {

    Boolean existsBySeasonIdAndItemId(Long seasonId, Long itemId);
}
