package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.service.UseDeckAugmentService;
import com.heekng.api_toche_web.service.UseDeckUnitAugmentService;
import com.heekng.api_toche_web.service.UseDeckUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UseDeckStatsProcessor implements ItemProcessor<MatchInfo, MatchInfo> {

    private final UseDeckAugmentService useDeckAugmentService;
    private final UseDeckUnitService useDeckUnitService;
    private final UseDeckUnitAugmentService useDeckUnitAugmentService;

    @Override
    public MatchInfo process(MatchInfo matchInfo) throws Exception {
        initializeUnitsAndAugmentsAndSeason(matchInfo);
        List<Augment> augments = getAugmentsByMatchInfo(matchInfo);
        List<Unit> units = getUnitsByMatchInfo(matchInfo);

        UseDeckAugment useDeckAugment = useDeckAugmentService.findOrSaveByAugmentsAndSeason(augments, matchInfo.getSeason());
        useDeckAugment.addUseCount();

        UseDeckUnit useDeckUnit = useDeckUnitService.findOrSaveByUnits(units);
        useDeckUnit.addUseCount();

        UseDeckUnitAugment useDeckUnitAugment = useDeckUnitAugmentService.findOrSaveByUseDeckUnitAndUseDeckAugment(useDeckUnit, useDeckAugment);
        useDeckUnitAugment.addUseCount();

        matchInfo.updateIsDeckCollectedByIsDeckCollected(true);
        return matchInfo;
    }

    private List<Unit> getUnitsByMatchInfo(MatchInfo matchInfo) {
        return matchInfo.getMatchUnits().stream()
                .map(MatchUnit::getUnit)
                .collect(Collectors.toList());
    }

    private List<Augment> getAugmentsByMatchInfo(MatchInfo matchInfo) {
        return matchInfo.getMatchAugments().stream()
                .map(MatchAugment::getAugment)
                .collect(Collectors.toList());
    }

    private void initializeUnitsAndAugmentsAndSeason(MatchInfo matchInfo) {
        matchInfo.getMatchUnits().forEach(Hibernate::initialize);
        matchInfo.getMatchUnits().stream().map(MatchUnit::getUnit)
                .forEach(Hibernate::initialize);
        matchInfo.getMatchAugments().forEach(Hibernate::initialize);
        matchInfo.getMatchAugments().stream().map(MatchAugment::getAugment)
                .forEach(Hibernate::initialize);
        Hibernate.initialize(matchInfo.getSeason());
    }
}
