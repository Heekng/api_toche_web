package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    Optional<Item> searchItemByRiotItemId(Integer ItemNum);

    List<Item> searchByItemsRequest(ItemDTO.ItemsRequest itemsRequest);

    List<Item> searchByItemsRequestContainsSeasonId(ItemDTO.ItemsRequest itemsRequest);

    Optional<ItemDTO.ItemDetailResponse> searchWithFromItemByItemId(Long itemId);
}
