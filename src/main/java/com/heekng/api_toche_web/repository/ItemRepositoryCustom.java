package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    Optional<Item> searchLastSeasonItemByRiotItemId(Integer ItemNum);

    List<Item> searchByItemsRequest(ItemDTO.ItemsRequest itemsRequest);

}
