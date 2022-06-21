package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.SeasonDTO;
import com.heekng.api_toche_web.entity.Season;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.heekng.api_toche_web.entity.QSeason.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class SeasonRepositoryImpl implements SeasonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Season> searchBySeasonsRequest(SeasonDTO.SeasonsRequest seasonsRequest) {
        return queryFactory
                .selectFrom(season)
                .where(
                        seasonNumEq(seasonsRequest.getSeasonNum()),
                        seasonNameContains(seasonsRequest.getSeasonName())
                )
                .orderBy(season.seasonNum.desc(), season.seasonName.desc())
                .fetch();
    }

    private BooleanExpression seasonNumEq(Integer seasonNum) {
        return seasonNum != null ? season.seasonNum.eq(seasonNum) : null;
    }

    private BooleanExpression seasonNameEq(String seasonName) {
        return hasText(seasonName) ? season.seasonName.eq(seasonName) : null;
    }

    private BooleanExpression seasonNameContains(String seasonName) {
        return hasText(seasonName) ? season.seasonName.contains(seasonName) : null;
    }
}
