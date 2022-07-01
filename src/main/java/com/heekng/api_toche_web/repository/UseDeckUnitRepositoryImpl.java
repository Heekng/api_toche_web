package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.heekng.api_toche_web.entity.QUnit.*;
import static com.heekng.api_toche_web.entity.QUseDeckUnit.*;
import static com.heekng.api_toche_web.entity.QUseUnit.*;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class UseDeckUnitRepositoryImpl implements UseDeckUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UseDeckUnit> searchByUnits(List<Unit> units) {
        Map<UseDeckUnit, List<UseUnit>> transform = queryFactory
                .selectFrom(useDeckUnit)
                .leftJoin(useDeckUnit.useUnits, useUnit)
                .innerJoin(useUnit.unit, unit)
                .where(
                        unitEqs(units)
                )
                .distinct()
                .transform(groupBy(useDeckUnit).as(list(useUnit)));
        Optional<UseDeckUnit> useDeckUnitOptional = transform.entrySet().stream()
                .filter(useDeckUnitListEntry -> {
                    Set<Unit> unitSet = useDeckUnitListEntry.getValue().stream()
                            .map(UseUnit::getUnit)
                            .collect(Collectors.toSet());
                    return unitSet.containsAll(units);
                })
                .filter(useDeckUnitListEntry -> useDeckUnitListEntry.getValue().size() == units.size())
                .map(Map.Entry::getKey)
                .findFirst();
        return useDeckUnitOptional;
    }

    private BooleanBuilder unitEqs(List<Unit> units) {
        BooleanBuilder builder = new BooleanBuilder();
        units.forEach(unitEntity -> builder.or(unitEq(unitEntity)));
        return builder;
    }

    private BooleanExpression unitEq(Unit unitEntity) {
        return unitEntity != null ? unit.eq(unitEntity) : null;
    }

}
