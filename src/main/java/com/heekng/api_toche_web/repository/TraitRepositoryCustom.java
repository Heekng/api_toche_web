package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.TraitDTO;
import com.heekng.api_toche_web.entity.Trait;

import java.util.List;

public interface TraitRepositoryCustom {

    List<Trait> searchByTraitsRequest(TraitDTO.TraitsRequest traitsRequest);

    List<Trait> searchByNamesAndSeasonId(List<String> names, Long seasonId);
}
