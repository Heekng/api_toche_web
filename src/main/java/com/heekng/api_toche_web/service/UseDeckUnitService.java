package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.UseDeckUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UseDeckUnitService {

    private final UseDeckUnitRepository useDeckUnitRepository;

    @Transactional
    public UseDeckUnit findOrSaveByUnits(List<Unit> units) {
        Optional<UseDeckUnit> useDeckUnitOptional = useDeckUnitRepository.searchByUnits(units);
        UseDeckUnit useDeckUnit;
        if (useDeckUnitOptional.isPresent()) {
            useDeckUnit = useDeckUnitOptional.get();
        } else {
            useDeckUnit = UseDeckUnit.builder()
                    .useCount(0L)
                    .build();
            units.stream()
                    .map(unit ->
                            UseUnit.builder()
                                    .unit(unit)
                                    .build()
                    )
                    .forEach(useDeckUnit::insertUseUnit);
            useDeckUnitRepository.save(useDeckUnit);
        }
        return useDeckUnit;
    }
}
