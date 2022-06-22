package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item findOrSave(String name, Integer num, Season season) {
        Optional<Item> itemOptional = itemRepository.findByName(name);
        Item item = null;
        if (itemOptional.isEmpty()) {
            item = Item.builder()
                    .name(name)
                    .num(num)
                    .build();
            itemRepository.save(item);
        } else {
            item = itemOptional.get();
        }
        return item;
    }

    public List<Item> 성(ItemDTO.ItemsRequest itemsRequest) {
        return itemsRequest.getSeasonId() == null ? itemRepository.searchByItemsRequest(itemsRequest) : itemRepository.searchByItemsRequestContainsSeasonId(itemsRequest);
    }
}
