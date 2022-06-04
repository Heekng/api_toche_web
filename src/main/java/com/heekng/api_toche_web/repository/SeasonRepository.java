package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long>, SeasonRepositoryCustom {

}
