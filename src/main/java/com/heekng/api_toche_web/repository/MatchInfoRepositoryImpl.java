package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import java.util.*;
import java.util.stream.Collectors;

import static com.heekng.api_toche_web.entity.QMatchInfo.*;
import static com.heekng.api_toche_web.entity.QMatchUnit.*;
import static com.heekng.api_toche_web.entity.QUnit.*;
import static com.querydsl.core.group.GroupBy.*;

@Slf4j
@RequiredArgsConstructor
public class MatchInfoRepositoryImpl implements MatchInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<MatchInfo> searchByUnitContains(List<Long> unitIds) {
        Map<MatchInfo, List<MatchUnit>> transform = queryFactory
                .select(matchInfo)
                .from(matchInfo)
                .leftJoin(matchInfo.matchUnits, matchUnit)
                .where(
                        unitContains(unitIds)
                )
                .distinct()
                .transform(groupBy(matchInfo).as(list(matchUnit)));
        List<MatchInfo> matchInfos = transform.entrySet().stream()
                .filter(matchInfoListEntry -> {
                    Set<Long> allUnitIds = matchInfoListEntry.getValue().stream()
                            .map(MatchUnit::getUnit)
                            .map(Unit::getId)
                            .collect(Collectors.toSet());
                    return allUnitIds.containsAll(unitIds);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        matchInfos.stream().map(MatchInfo::getMatchUnits).forEach(Hibernate::initialize);
        matchInfos.stream().map(MatchInfo::getMatchUnits).forEach(matchUnits ->
                matchUnits.stream().map(MatchUnit::getUnit).forEach(Hibernate::initialize)
        );
        return matchInfos;
    }

    private BooleanBuilder unitContains(List<Long> unitIds) {
        BooleanBuilder builder = new BooleanBuilder();
        unitIds.forEach(unitId -> builder.or(unitIdContains(unitId)));
        return builder;
    }

    private BooleanExpression unitIdContains(Long unitId) {
        return unitId != null ? matchUnit.unit.id.eq(unitId) : null;
    }
}
