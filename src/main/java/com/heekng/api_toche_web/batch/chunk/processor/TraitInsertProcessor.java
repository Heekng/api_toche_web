package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.domain.TraitInsertVO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
@Slf4j
public class TraitInsertProcessor implements ItemProcessor<TraitInsertVO, Trait> {

    private final Season season;

    @Override
    public Trait process(TraitInsertVO traitInsertVO) throws Exception {
        return traitInsertVO.toEntity(season);
    }
}
