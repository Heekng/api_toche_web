package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static com.heekng.api_toche_web.entity.QAugment.*;
import static com.heekng.api_toche_web.entity.QMatchAugment.matchAugment;
import static com.heekng.api_toche_web.entity.QMatchInfo.matchInfo;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static com.heekng.api_toche_web.entity.QUseAugment.*;
import static com.heekng.api_toche_web.entity.QUseDeckAugment.*;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class UseDeckAugmentRepositoryImpl implements UseDeckAugmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UseDeckAugment> searchByAugmentsAndSeason(List<Augment> augments, Season seasonEntity) {
        Map<UseDeckAugment, List<UseAugment>> transform = queryFactory
                .selectFrom(useDeckAugment)
                .innerJoin(useDeckAugment.season, season)
                .on(
                        season.eq(seasonEntity)
                )
                .leftJoin(useDeckAugment.useAugments, useAugment)
                .innerJoin(useAugment.augment, augment)
                .where(
                        augmentEqs(augments)
                )
                .distinct()
                .transform(groupBy(useDeckAugment).as(list(useAugment)));
        Optional<UseDeckAugment> useDeckAugmentOptional = transform.entrySet().stream()
                .filter(useDeckAugmentListEntry -> {
                    Set<Augment> augmentSet = useDeckAugmentListEntry.getValue().stream()
                            .map(UseAugment::getAugment)
                            .collect(Collectors.toSet());
                    return augmentSet.containsAll(augments);
                })
                .filter(useDeckAugmentListEntry -> useDeckAugmentListEntry.getValue().size() == augments.size())
                .map(Map.Entry::getKey)
                .findFirst();

        return useDeckAugmentOptional;
    }

    @Override
    public List<UseDeckAugment> searchAugmentContainsByAugmentIdsAndSeasonId(List<Long> augmentIds, Long seasonId) {
        Map<UseDeckAugment, List<Long>> transform = queryFactory
                .selectFrom(useDeckAugment)
                .innerJoin(useDeckAugment.season, season)
                .on(season.id.eq(seasonId))
                .leftJoin(useDeckAugment.useAugments, useAugment)
                .leftJoin(useAugment.augment, augment)
                .where(
                        augmentIdEqs(augmentIds)
                )
                .transform(groupBy(useDeckAugment).as(list(augment.id)));
        List<UseDeckAugment> useDeckAugments = transform.entrySet().stream()
                .filter(useDeckAugmentListEntry ->
                        new HashSet<>(useDeckAugmentListEntry.getValue()).containsAll(augmentIds)
                )
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(UseDeckAugment::getUseCount).reversed())
                .collect(Collectors.toList());
        return useDeckAugments;
    }

    private BooleanBuilder augmentEqs(List<Augment> augments) {
        BooleanBuilder builder = new BooleanBuilder();
        augments.forEach(augmentEntity -> builder.or(augmentEq(augmentEntity)));
        return builder;
    }

    private BooleanExpression augmentEq(Augment augmentEntity) {
        return augmentEntity != null ? augment.eq(augmentEntity) : null;
    }

    private BooleanBuilder augmentIdEqs(List<Long> augmentIds) {
        BooleanBuilder builder = new BooleanBuilder();
        augmentIds.forEach(augmentId -> builder.or(augmentIdEq(augmentId)));
        return builder;
    }

    private BooleanExpression augmentIdEq(Long augmentId) {
        return augmentId != null ? augment.id.eq(augmentId) : null;
    }
}
