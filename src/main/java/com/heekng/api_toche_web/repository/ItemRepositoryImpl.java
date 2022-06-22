package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.heekng.api_toche_web.entity.QItem.*;
import static com.heekng.api_toche_web.entity.QMatchInfo.matchInfo;
import static com.heekng.api_toche_web.entity.QMatchItem.*;
import static com.heekng.api_toche_web.entity.QMatchUnit.*;
import static com.heekng.api_toche_web.entity.QSeason.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Item> searchItemByRiotItemId(Integer itemNum) {
        return Optional.ofNullable(
                queryFactory
                    .select(item)
                    .from(item)
                    .where(
                            item.num.eq(itemNum), item.name.isNotNull()
                    )
                    .fetchFirst()
        );
    }

    @Override
    public List<Item> searchByItemsRequest(ItemDTO.ItemsRequest itemsRequest) {
        return queryFactory
                .selectFrom(item)
                .where(
                        itemNameContains(itemsRequest.getItemName()),
                        itemNumEq(itemsRequest.getItemNum())
                )
                .orderBy(
                        item.num.asc()
                )
                .fetch();
    }

    @Override
    public List<Item> searchByItemsRequestContainsSeasonId(ItemDTO.ItemsRequest itemsRequest) {
        return queryFactory
                .select(item)
                .from(matchInfo)
                .leftJoin(matchInfo.season, season)
                .leftJoin(matchInfo.matchUnits, matchUnit)
                .leftJoin(matchUnit.matchItems, matchItem)
                .leftJoin(matchItem.item, item)
                .where(
                        item.id.isNotNull(),
                        seasonIdEq(itemsRequest.getSeasonId()),
                        itemNameContains(itemsRequest.getItemName()),
                        itemNumEq(itemsRequest.getItemNum())
                )
                .orderBy(
                        item.num.asc(),
                        item.name.asc()
                )
                .distinct()
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
