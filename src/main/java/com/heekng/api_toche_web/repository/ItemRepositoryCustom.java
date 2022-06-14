package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Item;

import java.util.Optional;

public interface ItemRepositoryCustom {

    Optional<Item> searchLastSeasonItemByRiotItemId(Integer ItemNum);

}
