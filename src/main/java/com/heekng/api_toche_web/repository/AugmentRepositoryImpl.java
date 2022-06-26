package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.heekng.api_toche_web.entity.QAugment.*;
import static com.heekng.api_toche_web.entity.QMatchAugment.*;
import static com.heekng.api_toche_web.entity.QMatchInfo.*;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class AugmentRepositoryImpl implements AugmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Augment> searchByAugmentsRequest(AugmentDTO.AugmentsRequest augmentsRequest) {
        return queryFactory
                .selectFrom(augment)
                .where(
                        augmentNameContains(augmentsRequest.getAugmentName())
                )
                .orderBy(
                        augment.name.asc()
                )
                .fetch();
    }

    @Override
    public Optional<Augment> searchByNameOrEnNameEq(String name, String enName) {
        return Optional.ofNullable(queryFactory
                .selectFrom(augment)
                .where(
                        augment.name.eq(name)
                                .or(augment.enName.eq(enName))
                )
                .orderBy(
                        augment.name.asc()
                )
                .fetchFirst());
    }

    @Override
    public List<Augment> searchByAugmentsRequestContainsSeasonId(AugmentDTO.AugmentsRequest augmentsRequest) {
        return queryFactory
                .select(augment)
                .from(matchInfo)
                .leftJoin(matchInfo.season, season)
                .leftJoin(matchInfo.matchAugments, matchAugment)
                .leftJoin(matchAugment.augment, augment)
                .where(
                        seasonIdEq(augmentsRequest.getSeasonId()),
                        augmentNameContains(augmentsRequest.getAugmentName())
                )
                .orderBy(
                        augment.name.asc()
                )
                .distinct()
                .fetch();
    }

    @Override
    public List<Augment> searchSeasonUsedAugmentBySeasonId(Long seasonId) {
        return queryFactory
                .select(augment)
                .from(matchInfo)
                .leftJoin(matchInfo.season, season)
                .leftJoin(matchInfo.matchAugments, matchAugment)
                .leftJoin(matchAugment.augment, augment)
                .on(augment.id.isNotNull())
                .where(
                        seasonIdEq(seasonId)
                )
                .orderBy(
                        augment.name.asc()
                )
                .distinct()
                .fetch();
    }

    private BooleanExpression augmentNameContains(String augmentName) {
        return hasText(augmentName) ? augment.name.contains(augmentName) : null;
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }
}
