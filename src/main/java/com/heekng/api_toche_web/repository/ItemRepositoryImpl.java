package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.dto.QItemDTO_ItemDetailResponse;
import com.heekng.api_toche_web.dto.QItemDTO_ItemsResponse;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.QItem;
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

    @Override
    public Optional<ItemDTO.ItemDetailResponse> searchWithFromItemByItemId(Long itemId) {
        QItem fromItem1 = new QItem("fromItem1");
        QItem fromItem2 = new QItem("fromItem2");
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QItemDTO_ItemDetailResponse(
                                        item.id,
                                        item.num,
                                        item.name,
                                        item.itemDesc,
                                        item.korName,
                                        new QItemDTO_ItemsResponse(
                                                fromItem1.id,
                                                fromItem1.num,
                                                fromItem1.name,
                                                fromItem1.itemDesc,
                                                fromItem1.korName,
                                                fromItem1.fromItem1,
                                                fromItem1.fromItem2,
                                                fromItem1.isUnique,
                                                fromItem1.iconPath
                                        ),
                                        new QItemDTO_ItemsResponse(
                                                fromItem2.id,
                                                fromItem2.num,
                                                fromItem2.name,
                                                fromItem2.itemDesc,
                                                fromItem2.korName,
                                                fromItem2.fromItem1,
                                                fromItem2.fromItem2,
                                                fromItem2.isUnique,
                                                fromItem2.iconPath
                                        ),
                                        item.isUnique,
                                        item.iconPath
                                )
                        )
                        .from(item)
                        .innerJoin(fromItem1)
                        .on(item.fromItem1.eq(fromItem1.num))
                        .innerJoin(fromItem2)
                        .on(item.fromItem2.eq(fromItem2.num))
                        .where(
                                itemIdEq(itemId)
                        )
                        .fetchOne()
        );
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

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? item.id.eq(itemId) : null;
    }
}
