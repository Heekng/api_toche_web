package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.heekng.api_toche_web.entity.QItem.*;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static org.springframework.util.StringUtils.*;

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

    @Override
    public List<Item> searchByItemsRequest(ItemDTO.ItemsRequest itemsRequest) {
        return queryFactory
                .selectFrom(item)
                .leftJoin(item.season, season)
                .fetchJoin()
                .where(
                        seasonIdEq(itemsRequest.getSeasonId()),
                        itemNameContains(itemsRequest.getItemName()),
                        itemNumEq(itemsRequest.getItemNum())
                )
                .orderBy(
                        season.seasonNum.desc(),
                        season.seasonName.desc(),
                        item.num.asc()
                )
                .fetch();
    }

    private BooleanExpression seasonIdEq(Long seasonId) {
        return seasonId != null ? season.id.eq(seasonId) : null;
    }

    private BooleanExpression itemNameContains(String itemName) {
        return hasText(itemName) ? item.name.contains(itemName) : null;
    }

    private BooleanExpression itemNumEq(Integer itemNum) {
        return itemNum != null ? item.num.eq(itemNum) : null;
    }
}
