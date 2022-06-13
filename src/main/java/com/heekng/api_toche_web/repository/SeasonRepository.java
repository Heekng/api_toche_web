package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long>, SeasonRepositoryCustom {
    Optional<Season> findBySeasonNum(Integer seasonNum);
}
