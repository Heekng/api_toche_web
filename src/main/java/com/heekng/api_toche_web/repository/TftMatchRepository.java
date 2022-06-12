package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.TftMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TftMatchRepository extends JpaRepository<TftMatch, Long>, TftMatchRepositoryCustom {
    Boolean existsByMatchId(String matchId);
}
