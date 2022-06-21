package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.ItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class ItemApiController {

    @Value("${cdragon.path.image}")
    private String CDRAGON_PATH_IMAGE;
    private final ModelMapper standardMapper;
    private final ItemRepository itemRepository;

    @GetMapping("/items")
    public List<ItemDTO.ItemsResponse> items(
            @ModelAttribute ItemDTO.ItemsRequest itemsRequest
    ) {
        List<Item> items = itemRepository.searchByItemsRequest(itemsRequest);
        return items.stream()
                .map(item -> {
                    ItemDTO.ItemsResponse itemsResponse = standardMapper.map(item, ItemDTO.ItemsResponse.class);
                    itemsResponse.setIconPath(CDRAGON_PATH_IMAGE + itemsResponse.getIconPath());
                    return itemsResponse;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/items/{itemId}")
    public ItemDTO.ItemDetailResponse itemByItemId(
            @PathVariable(name = "itemId", required = true) Long itemId
    ) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Item 입니다."));
        return standardMapper.map(item, ItemDTO.ItemDetailResponse.class);
    }
}
