package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.entity.Augment;

import java.util.List;
import java.util.Optional;

public interface AugmentRepositoryCustom {

    List<Augment> searchByAugmentsRequest(AugmentDTO.AugmentsRequest augmentsRequest);

    Optional<Augment> searchByNameOrEnNameEq(String name, String enName);

    List<Augment> searchByAugmentsRequestContainsSeasonId(AugmentDTO.AugmentsRequest augmentsRequest);
}
