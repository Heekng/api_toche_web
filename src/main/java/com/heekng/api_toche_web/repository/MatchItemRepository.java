package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchItemRepository extends JpaRepository<MatchItem, Long>, MatchItemRepositoryCustom {

    Long countByItemId(Long itemId);

}
