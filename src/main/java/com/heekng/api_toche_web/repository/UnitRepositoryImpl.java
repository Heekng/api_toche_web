package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.QSeason;
import com.heekng.api_toche_web.entity.Unit;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

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
                        season.seasonNum.desc(),
                        season.seasonName.desc(),
                        unit.name.asc()
                )
                .fetch();
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }

    private BooleanExpression unitNameContain(String unitName) {
        return hasText(unitName) ? unit.name.contains(unitName) : null;
    }
}
