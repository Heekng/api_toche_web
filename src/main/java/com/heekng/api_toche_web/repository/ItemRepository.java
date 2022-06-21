package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Optional<Item> findByName(String name);

    Optional<Item> findByNum(Integer num);
}
