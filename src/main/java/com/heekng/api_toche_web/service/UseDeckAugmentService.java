package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.UseAugment;
import com.heekng.api_toche_web.entity.UseDeckAugment;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.UseDeckAugmentRepository;
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
public class UseDeckAugmentService {

    private final UseDeckAugmentRepository useDeckAugmentRepository;

    @Transactional
    public UseDeckAugment findOrSaveByAugmentsAndSeason(List<Augment> augments, Season season) {
        Optional<UseDeckAugment> useDeckAugmentOptional = useDeckAugmentRepository.searchByAugmentsAndSeason(augments, season);
        UseDeckAugment useDeckAugment;
        if (useDeckAugmentOptional.isPresent()) {
            useDeckAugment = useDeckAugmentOptional.get();
        } else {
            useDeckAugment = UseDeckAugment.builder()
                    .season(season)
                    .useCount(0L)
                    .build();
            augments.stream()
                    .map(augment ->
                        UseAugment.builder()
                                .augment(augment)
                                .build()
                    )
                    .forEach(useDeckAugment::insertUseAugment);
            useDeckAugmentRepository.save(useDeckAugment);
        }
        return useDeckAugment;
    }
}
