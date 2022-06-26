package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.AugmentRepository;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.SeasonAugmentRepository;
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
public class SeasonAugmentProcessor implements ItemProcessor<Season, List<SeasonAugment>> {

    private final AugmentRepository augmentRepository;
    private final SeasonAugmentRepository seasonAugmentRepository;

    @Override
    public List<SeasonAugment> process(Season season) throws Exception {

        List<Augment> augments = augmentRepository.searchSeasonUsedAugmentBySeasonId(season.getId());

        return augments.stream()
                .filter(Objects::nonNull)
                .filter(augment -> !seasonAugmentRepository.existsBySeasonIdAndAugmentId(season.getId(), augment.getId()))
                .map(augment -> SeasonAugment.builder()
                        .season(season)
                        .augment(augment)
                        .build()
                )
                .collect(Collectors.toList());
    }
}
