package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonItemDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonSetDataDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CDragonSeasonInsertProcessor implements ItemProcessor<CDragonSetDataDTO, Season> {

    private final SeasonRepository seasonRepository;

    @Override
    public Season process(CDragonSetDataDTO cDragonSetDataDTO) throws Exception {

        Optional<Season> seasonOptional = seasonRepository.findBySeasonName(cDragonSetDataDTO.getMutator());

        Season season = seasonOptional.orElse(
                Season.builder()
                        .seasonName(cDragonSetDataDTO.getMutator())
                        .seasonNum(cDragonSetDataDTO.getNumber())
                        .build()
        );

        return season;
    }
}
