package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.dto.GuidDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.MatchInfo;
import com.heekng.api_toche_web.entity.MatchUnit;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.MatchInfoRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
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

    public GuidDTO.GuidResultResponse guidByUnits(UnitDTO.GuidRequest guidRequest) {
        // 전체 리스트
        List<MatchInfo> matchInfos = matchInfoRepository.searchByUnitContains(guidRequest.getUnitIds());
        return filterGuidResultByMatchInfos(matchInfos);
    }

    public GuidDTO.GuidResultResponse guidByAugments(AugmentDTO.GuidRequest guidRequest) {
        // 전체 리스트
        List<MatchInfo> matchInfos = matchInfoRepository.searchByAugmentContains(guidRequest.getAugmentIds());
        return filterGuidResultByMatchInfos(matchInfos);
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
                .allUsedCount(matchInfos.size())
                .build();
    }
}
