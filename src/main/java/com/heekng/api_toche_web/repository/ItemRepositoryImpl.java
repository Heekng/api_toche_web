package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.*;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.QItem;
import com.heekng.api_toche_web.entity.QSeasonItem;
import com.querydsl.core.BooleanBuilder;
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
import static com.heekng.api_toche_web.entity.QSeasonItem.*;
import static com.heekng.api_toche_web.entity.QUnit.unit;
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
    public List<Item> searchSeasonUsedItemBySeasonId(Long seasonId) {
        return queryFactory
                .select(item)
                .from(matchInfo)
                .innerJoin(matchInfo.season, season)
                .leftJoin(matchInfo.matchUnits, matchUnit)
                .leftJoin(matchUnit.matchItems, matchItem)
                .innerJoin(matchItem.item, item)
                .on(item.id.isNotNull())
                .where(
                        seasonIdEq(seasonId)
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
                                        item.krName,
                                        new QItemDTO_ItemsResponse(
                                                fromItem1.id,
                                                fromItem1.num,
                                                fromItem1.name,
                                                fromItem1.itemDesc,
                                                fromItem1.krName,
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
                                                fromItem2.krName,
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

    @Override
    public List<ItemDTO.UnitRankResponse> searchItemRankByUnitId(Long unitId) {
        return queryFactory
                .select(
                        new QItemDTO_UnitRankResponse(
                                item.id,
                                item.num,
                                item.name,
                                item.itemDesc,
                                item.krName,
                                item.fromItem1,
                                item.fromItem2,
                                item.isUnique,
                                item.iconPath,
                                item.count()
                        )
                )
                .from(matchUnit)
                .innerJoin(matchUnit.unit, unit)
                .on(
                        unitIdEq(unitId)
                )
                .leftJoin(matchUnit.matchItems, matchItem)
                .innerJoin(matchItem.item, item)
                .groupBy(item)
                .orderBy(item.count().desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Item> searchByNums(List<Integer> nums) {
        return queryFactory
                .selectFrom(item)
                .where(
                        itemNumsEq(nums)
                )
                .fetch();
    }

    @Override
    public List<Item> searchBySeasonId(Long seasonId) {
        return queryFactory
                .select(item)
                .from(seasonItem)
                .innerJoin(seasonItem.season, season)
                .on(
                        seasonIdEq(seasonId)
                )
                .innerJoin(seasonItem.item, item)
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

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? item.id.eq(itemId) : null;
    }

    private BooleanExpression unitIdEq(Long unitId) {
        return unitId != null ? unit.id.eq(unitId) : null;
    }

    private BooleanBuilder itemNumsEq(List<Integer> itemNums) {
        BooleanBuilder builder = new BooleanBuilder();
        itemNums.forEach(itemNum -> {
            builder.or(itemNumEq(itemNum));
        });
        return builder;
    }
}
