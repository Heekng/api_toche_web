package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.dto.GuidDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GuidService {

    private final MatchInfoRepository matchInfoRepository;
    private final ModelMapper standardMapper;
    private final UseDeckUnitRepository useDeckUnitRepository;
    private final UseDeckAugmentRepository useDeckAugmentRepository;
    private final UseDeckUnitAugmentRepository useDeckUnitAugmentRepository;

    public GuidDTO.GuidResultResponse guidByUnits(UnitDTO.GuidRequest guidRequest) {
        List<UseDeckUnit> useDeckUnits = useDeckUnitRepository.searchUnitContainsByUnitIds(guidRequest.getUnitIds());
        return getGuidResultResponseByUseDeckUnits(useDeckUnits);
    }

    public GuidDTO.GuidResultResponse guidByAugments(AugmentDTO.GuidRequest guidRequest) {
        List<UseDeckAugment> useDeckAugments = useDeckAugmentRepository.searchAugmentContainsByAugmentIdsAndSeasonId(guidRequest.getAugmentIds(), guidRequest.getSeasonId());
        List<UseDeckUnitAugment> useDeckUnitAugments = getUseDeckUnitAugmentsByUseDeckAugments(useDeckAugments);
        return getGuidResultResponseByUseDeckUnitAugments(useDeckUnitAugments);
    }

    private GuidDTO.GuidResultResponse getGuidResultResponseByUseDeckUnitAugments(List<UseDeckUnitAugment> useDeckUnitAugments) {
        List<UseDeckUnit> useDeckUnits = useDeckUnitAugments.stream()
                .map(UseDeckUnitAugment::getUseDeckUnit)
                .sorted(Comparator.comparing(UseDeckUnit::getUseCount).reversed())
                .collect(Collectors.toList());
        return getGuidResultResponseByUseDeckUnits(useDeckUnits);
    }

    private List<UseDeckUnitAugment> getUseDeckUnitAugmentsByUseDeckAugments(List<UseDeckAugment> useDeckAugments) {
        List<UseDeckUnitAugment> useDeckUnitAugments = new ArrayList<>();
        useDeckAugments.stream()
                .map(useDeckUnitAugmentRepository::findByUseDeckAugment)
                .forEach(useDeckUnitAugments::addAll);
        useDeckUnitAugments.sort(Comparator.comparing(UseDeckUnitAugment::getUseCount).reversed());
        return useDeckUnitAugments;
    }

    private GuidDTO.GuidResultResponse getGuidResultResponseByUseDeckUnits(List<UseDeckUnit> useDeckUnits) {
        GuidDTO.GuidResultResponse guidResultResponse;
        if (useDeckUnits.isEmpty()) {
            guidResultResponse = GuidDTO.GuidResultResponse.builder()
                    .allUsedCount(0L)
                    .resultCount(0L)
                    .units(null)
                    .build();
        } else {
            Long allUseCount = useDeckUnits.stream()
                    .mapToLong(UseDeckUnit::getUseCount)
                    .sum();
            UseDeckUnit useDeckUnit = useDeckUnits.get(0);
            Long resultCount = useDeckUnit.getUseCount();
            List<UnitDTO.UnitsResponse> unitsResponse = useDeckUnit.getUseUnits().stream()
                    .map(UseUnit::getUnit)
                    .map(unit -> standardMapper.map(unit, UnitDTO.UnitsResponse.class))
                    .collect(Collectors.toList());
            guidResultResponse = GuidDTO.GuidResultResponse.builder()
                    .allUsedCount(allUseCount)
                    .resultCount(resultCount)
                    .units(unitsResponse)
                    .build();
        }
        return guidResultResponse;
    }

    private GuidDTO.GuidResultResponse filterGuidResultByMatchInfos(List<MatchInfo> matchInfos) {
        // unit list
        List<List<Unit>> unitsList = matchInfos.stream()
                .map(matchInfo -> matchInfo.getMatchUnits().stream().map(MatchUnit::getUnit).collect(Collectors.toList()))
                .collect(Collectors.toList());
        // 정렬
        unitsList.forEach(unitList -> unitList.sort(Comparator.comparing(Unit::getId)));
        // 각 유닛 리스트 그룹화
        Map<List<Unit>, Long> unitsCountMap = unitsList.stream()
                .collect(Collectors.groupingBy(unitList -> unitList, Collectors.counting()));
        // 최다 사용 덱
        Map.Entry<List<Unit>, Long> resultEntry = unitsCountMap.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElse(new AbstractMap.SimpleEntry<>(new ArrayList<>(), 0L));

        List<UnitDTO.UnitsResponse> unitsResponseList = resultEntry.getKey().stream()
                .map(unit -> standardMapper.map(unit, UnitDTO.UnitsResponse.class))
                .collect(Collectors.toList());

        return GuidDTO.GuidResultResponse.builder()
                .units(unitsResponseList)
                .resultCount(resultEntry.getValue())
                .allUsedCount((long) matchInfos.size())
                .build();
    }
}
