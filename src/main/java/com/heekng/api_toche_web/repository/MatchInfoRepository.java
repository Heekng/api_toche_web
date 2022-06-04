package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Match;
import com.heekng.api_toche_web.entity.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long>, MatchInfoRepositoryCustom {

}
