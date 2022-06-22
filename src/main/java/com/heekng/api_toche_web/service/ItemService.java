package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.MatchItemRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final MatchItemRepository matchItemRepository;
    private final UnitRepository unitRepository;

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

    public List<Item> findByItemsRequest(ItemDTO.ItemsRequest itemsRequest) {
        return itemsRequest.getSeasonId() == null ? itemRepository.searchByItemsRequest(itemsRequest) : itemRepository.searchByItemsRequestContainsSeasonId(itemsRequest);
    }

    public ItemDTO.ItemDetailResponse findItemDetail(Long itemId) {
        ItemDTO.ItemDetailResponse itemDetailResponse = itemRepository.searchWithFromItemByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Item 입니다."));
        Long itemUsedCount = matchItemRepository.countByItemId(itemId);
        itemDetailResponse.setUsedCount(itemUsedCount);
//        List<UnitDTO.ItemRankResponse> itemRankResponses = unitRepository.searchUnitRankByItemId(itemId);
//        itemDetailResponse.getUsedRankUnits().addAll(itemRankResponses);
        return itemDetailResponse;
    }
}
