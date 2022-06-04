package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

}
