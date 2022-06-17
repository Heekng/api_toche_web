package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    @Transactional
    public Unit findOrSave(String name, Integer rarity, Season season) {
        Optional<Unit> unitOptional = unitRepository.findByNameAndSeasonId(name, season.getId());
        Unit unit = null;
        if (unitOptional.isEmpty()) {
            unit = Unit.builder()
                    .name(name)
                    .rarity(rarity)
                    .season(season)
                    .build();
            unitRepository.save(unit);
        } else {
            unit = unitOptional.get();
        }
        return unit;
    }

    public Boolean isExistUnits(List<Long> unitIds) {
        List<Optional<Unit>> unitOptionalList = unitIds.stream()
                .map(unitRepository::findById)
                .collect(Collectors.toList());
        for (Optional<Unit> unit : unitOptionalList) {
            if (unit.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
