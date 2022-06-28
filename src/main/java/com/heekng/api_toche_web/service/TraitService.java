package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.TraitDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.TraitRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TraitService {

    private final TraitRepository traitRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper standardMapper;

    @Transactional
    public Trait findOrSave(String name, Integer tierTotalCount, Season season) {
        Optional<Trait> traitOptional = traitRepository.findByNameAndSeasonId(name, season.getId());
        Trait trait = null;
        if (traitOptional.isEmpty()) {
            trait = Trait.builder()
                    .name(name)
                    .tierTotalCount(tierTotalCount)
                    .season(season)
                    .build();
            traitRepository.save(trait);
        } else {
            trait = traitOptional.get();
        }
        return trait;
    }

    public TraitDTO.TraitDetailResponse findTraitDetail(Long traitId) {
        Trait trait = traitRepository.findWithSeasonById(traitId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Trait 입니다."));
        List<Unit> units = unitRepository.searchByTraitId(traitId);
        List<UnitDTO.TraitDetailResponse> unitTraitDetailResponses = mapUnitToTraitDetailResponse(units);
        TraitDTO.TraitDetailResponse traitDetailResponse = standardMapper.map(trait, TraitDTO.TraitDetailResponse.class);
        traitDetailResponse.insertUnits(unitTraitDetailResponses);
        return traitDetailResponse;
    }

    private List<UnitDTO.TraitDetailResponse> mapUnitToTraitDetailResponse(List<Unit> units) {
        return units.stream()
                .map(unit -> standardMapper.map(unit, UnitDTO.TraitDetailResponse.class))
                .collect(Collectors.toList());
    }
}
