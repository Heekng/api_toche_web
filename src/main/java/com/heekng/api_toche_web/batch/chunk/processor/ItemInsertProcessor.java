package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.domain.ItemInsertVO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class ItemInsertProcessor implements ItemProcessor<ItemInsertVO, Item> {

    private final Season season;

    @Override
    public Item process(ItemInsertVO itemInsertVO) throws Exception {
        return itemInsertVO.toEntity(season);
    }
}
