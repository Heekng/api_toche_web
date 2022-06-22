package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Unit;

import java.util.List;

public interface UnitRepositoryCustom {

    List<Unit> searchByUnitsRequest(UnitDTO.UnitsRequest unitsRequest);

    List<UnitDTO.ItemRankResponse> searchUnitRankByItemId(Long itemId);
}
