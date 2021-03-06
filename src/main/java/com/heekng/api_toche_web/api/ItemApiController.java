package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import com.heekng.api_toche_web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class ItemApiController {

    private final ModelMapper standardMapper;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UnitRepository unitRepository;

    @GetMapping("/items")
    public List<ItemDTO.ItemsResponse> items(
            @ModelAttribute ItemDTO.ItemsRequest itemsRequest
    ) {
        List<Item> items = itemService.findByItemsRequest(itemsRequest);
        return items.stream()
                .map(item -> standardMapper.map(item, ItemDTO.ItemsResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/items/{itemId}")
    public ItemDTO.ItemDetailResponse itemByItemId(
            @PathVariable(name = "itemId", required = true) Long itemId
    ) {
        return itemService.findItemDetail(itemId);
    }

    @GetMapping("/items/{itemId}/mostUnits")
    public List<UnitDTO.ItemRankResponse> mostUnitsByItemId(
            @PathVariable(name = "itemId", required = true) Long itemId,
            @Validated ItemDTO.MostUnitRequest mostUnitRequest
    ) {
        return unitRepository.searchUnitRankByItemId(itemId, mostUnitRequest.getSeasonId());
    }

}
