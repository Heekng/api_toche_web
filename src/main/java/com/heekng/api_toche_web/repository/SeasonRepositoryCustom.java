package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.SeasonDTO;
import com.heekng.api_toche_web.entity.Season;

import java.util.List;

public interface SeasonRepositoryCustom {

    List<Season> searchBySeasonsRequest(SeasonDTO.SeasonsRequest seasonsRequest);

}
