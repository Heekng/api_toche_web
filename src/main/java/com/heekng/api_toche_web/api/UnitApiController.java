package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import com.heekng.api_toche_web.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class UnitApiController {

    private final UnitService unitService;
    private final UnitRepository unitRepository;
    private final ModelMapper standardMapper;
    private final ItemRepository itemRepository;

    @GetMapping("/units")
    public List<UnitDTO.UnitsResponse> units(
            @ModelAttribute UnitDTO.UnitsRequest unitsRequest
    ) {
        List<Unit> units = unitRepository.searchByUnitsRequest(unitsRequest);
        return units.stream()
                .map(unit -> standardMapper.map(unit, UnitDTO.UnitsResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/units/{unitId}")
    public UnitDTO.UnitDetailResponse unitByUnitId(
            @PathVariable("unitId") Long unitId
    ) {
        Unit unit = unitService.findDetailByUnitId(unitId);
        return new UnitDTO.UnitDetailResponse(unit);
    }

    @GetMapping("/units/{unitId}/mostItems")
    public List<ItemDTO.UnitRankResponse> mostItemsByUnitId(
            @PathVariable("unitId") Long unitId
    ) {
        return itemRepository.searchItemRankByUnitId(unitId);
    }

}
