package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    Optional<Item> searchItemByRiotItemId(Integer ItemNum);

    List<Item> searchByItemsRequest(ItemDTO.ItemsRequest itemsRequest);

    List<Item> searchByItemsRequestContainsSeasonId(ItemDTO.ItemsRequest itemsRequest);

    List<Item> searchSeasonUsedItemBySeasonId(Long seasonId);

    Optional<ItemDTO.ItemDetailResponse> searchWithFromItemByItemId(Long itemId);

    List<ItemDTO.UnitRankResponse> searchItemRankByUnitId(Long unitId);

    List<Item> searchByNums(List<Integer> nums);
}
