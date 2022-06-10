package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.domain.UnitInsertVO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.TraitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class UnitInsertProcessor implements ItemProcessor<UnitInsertVO, Unit> {

    private final Season season;
    private final TraitRepository traitRepository;

    @Override
    public Unit process(UnitInsertVO unitInsertVO) throws Exception {
        Unit unit = unitInsertVO.toEntity(season);
        List<UnitTrait> unitTraits = unitInsertVO.getTraits().stream()
                .map(traitName ->
                        traitRepository.findBySeasonSeasonIdAndNameContaining(
                                season.getSeasonId(),
                                traitName.substring(traitName.indexOf("_") + 1)
                        )
                ).map(trait ->
                        UnitTrait.builder()
                                .unit(unit)
                                .trait(trait)
                                .build()
                ).collect(Collectors.toList());
        unit.addUnitTraits(unitTraits);
        return unit;
    }
}
