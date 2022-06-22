package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.QUnitDTO_ItemRankResponse;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.heekng.api_toche_web.entity.QItem.*;
import static com.heekng.api_toche_web.entity.QMatchItem.*;
import static com.heekng.api_toche_web.entity.QMatchUnit.*;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static com.heekng.api_toche_web.entity.QUnit.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class UnitRepositoryImpl implements UnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Unit> searchByUnitsRequest(UnitDTO.UnitsRequest unitsRequest) {
        return queryFactory
                .selectFrom(unit)
                .leftJoin(unit.season, season)
                .fetchJoin()
                .where(
                        seasonIdEq(unitsRequest.getSeasonId()),
                        unitNameContain(unitsRequest.getUnitName())
                )
                .orderBy(
                        unit.cost.asc(),
                        season.seasonNum.desc(),
                        season.seasonName.desc(),
                        unit.name.asc()
                )
                .fetch();
    }

    @Override
    public List<UnitDTO.ItemRankResponse> searchUnitRankByItemId(Long itemId) {
        return queryFactory
                .select(
                        new QUnitDTO_ItemRankResponse(
                                unit.id,
                                unit.name,
                                unit.krName,
                                unit.rarity,
                                unit.tier,
                                unit.cost,
                                unit.iconPath,
                                unit.count()
                        )
                )
                .from(matchUnit)
                .innerJoin(matchUnit.unit, unit)
                .leftJoin(matchUnit.matchItems, matchItem)
                .innerJoin(matchItem.item, item)
                .where(
                        itemIdEq(itemId)
                )
                .groupBy(unit)
                .orderBy(unit.count().desc())
                .limit(5)
                .fetch();
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }

    private BooleanExpression unitNameContain(String unitName) {
        return hasText(unitName) ? unit.name.contains(unitName) : null;
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? item.id.eq(itemId) : null;
    }
}
