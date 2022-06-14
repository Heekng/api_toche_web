package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item findOrSave(String name, Integer num, Season season) {
        Optional<Item> itemOptional = itemRepository.findByNameAndSeasonId(name, season.getId());
        Item item = null;
        if (itemOptional.isEmpty()) {
            item = Item.builder()
                    .season(season)
                    .name(name)
                    .num(num)
                    .build();
            itemRepository.save(item);
        } else {
            item = itemOptional.get();
        }
        return item;
    }
}
