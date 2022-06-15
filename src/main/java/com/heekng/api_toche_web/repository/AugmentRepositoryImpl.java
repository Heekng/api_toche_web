package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.QAugment;
import com.heekng.api_toche_web.entity.QSeason;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.heekng.api_toche_web.entity.QAugment.*;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class AugmentRepositoryImpl implements AugmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Augment> searchByAugmentsRequest(AugmentDTO.AugmentsRequest augmentsRequest) {
        return queryFactory
                .selectFrom(augment)
                .leftJoin(augment.season, season)
                .fetchJoin()
                .where(
                        augmentNameContains(augmentsRequest.getAugmentName()),
                        seasonIdEq(augmentsRequest.getSeasonId())
                )
                .orderBy(
                        season.seasonNum.desc(),
                        season.seasonName.desc(),
                        augment.name.asc()
                )
                .fetch();
    }

    private BooleanExpression augmentNameContains(String augmentName) {
        return hasText(augmentName) ? augment.name.contains(augmentName) : null;
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }
}
