package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.TftMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<TftMatch, Long>, MatchRepositoryCustom{

}
