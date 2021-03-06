package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.TraitDTO;
import com.heekng.api_toche_web.entity.QSeason;
import com.heekng.api_toche_web.entity.QTrait;
import com.heekng.api_toche_web.entity.Trait;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.heekng.api_toche_web.entity.QSeason.*;
import static com.heekng.api_toche_web.entity.QTrait.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class TraitRepositoryImpl implements TraitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Trait> searchByTraitsRequest(TraitDTO.TraitsRequest traitsRequest) {
        return queryFactory
                .selectFrom(trait)
                .leftJoin(trait.season, season)
                .fetchJoin()
                .where(
                        traitNameContains(traitsRequest.getTraitName()),
                        seasonIdEq(traitsRequest.getSeasonId())
                )
                .orderBy(
                        season.seasonNum.desc(),
                        season.seasonName.desc(),
                        trait.name.asc()
                )
                .fetch();
    }

    @Override
    public List<Trait> searchByNamesAndSeasonId(List<String> names, Long seasonId) {
        return queryFactory
                .selectFrom(trait)
                .leftJoin(trait.season, season)
                .where(
                        seasonIdEq(seasonId)
                                .and(traitNamesEq(names))
                )
                .fetch();
    }

    private BooleanExpression traitNameContains(String traitName) {
        return hasText(traitName) ? trait.name.contains(traitName) : null;
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }

    private BooleanExpression traitNameEq(String traitName) {
        return hasText(traitName) ? trait.name.eq(traitName) : null;
    }

    private BooleanBuilder traitNamesEq(List<String> traitNames) {
        BooleanBuilder builder = new BooleanBuilder();
        traitNames.forEach(traitName -> builder.or(traitNameEq(traitName)));
        return builder;
    }
}
