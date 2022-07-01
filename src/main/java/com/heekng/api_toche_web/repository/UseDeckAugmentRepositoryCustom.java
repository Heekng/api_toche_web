package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseDeckAugment;

import java.util.List;
import java.util.Optional;

public interface UseDeckAugmentRepositoryCustom {

    Optional<UseDeckAugment> searchByAugmentsAndSeason(List<Augment> augments, Season season);
}
