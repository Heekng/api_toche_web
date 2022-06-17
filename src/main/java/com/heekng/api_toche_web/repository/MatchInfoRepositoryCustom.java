package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchInfo;

import java.util.List;

public interface MatchInfoRepositoryCustom {

    List<MatchInfo> searchByUnitContains(List<Long> unitIds);
}
