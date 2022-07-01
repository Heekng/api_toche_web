package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;

import java.util.List;
import java.util.Optional;

public interface UseDeckUnitRepositoryCustom {

    Optional<UseDeckUnit> searchByUnits(List<Unit> units);
}
