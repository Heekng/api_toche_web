package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchRepositoryCustom{

}