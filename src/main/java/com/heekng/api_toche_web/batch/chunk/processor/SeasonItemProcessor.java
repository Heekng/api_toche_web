package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonSetDataDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.SeasonItem;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.SeasonItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeasonItemProcessor implements ItemProcessor<Season, List<SeasonItem>> {

    private final ItemRepository itemRepository;
    private final SeasonItemRepository seasonItemRepository;

    @Override
    public List<SeasonItem> process(Season season) throws Exception {

        List<Item> items = itemRepository.searchSeasonUsedItemBySeasonId(season.getId());

        return items.stream()
                .filter(Objects::nonNull)
                .filter(item -> !seasonItemRepository.existsBySeasonIdAndItemId(season.getId(), item.getId()))
                .map(item -> SeasonItem.builder()
                        .item(item)
                        .season(season)
                        .build())
                .collect(Collectors.toList());
    }
}
