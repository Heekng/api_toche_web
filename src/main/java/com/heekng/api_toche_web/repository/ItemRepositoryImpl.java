package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.QItem;
import com.heekng.api_toche_web.entity.QSeason;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.heekng.api_toche_web.entity.QItem.*;
import static com.heekng.api_toche_web.entity.QSeason.*;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Item> searchLastSeasonItemByRiotItemId(Integer itemNum) {
        return Optional.ofNullable(
                queryFactory
                    .select(item)
                    .from(item)
                    .innerJoin(item.season, season)
                    .where(
                            item.num.eq(itemNum), item.name.isNotNull()
                    )
                    .orderBy(season.seasonNum.desc(), season.seasonName.desc())
                    .fetchFirst()
        );
    }
}
