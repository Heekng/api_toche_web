package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.AugmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AugmentService {

    private final AugmentRepository augmentRepository;

    @Transactional
    public Augment findOrSave(String name, Season season) {
        Optional<Augment> augmentOptional = augmentRepository.findByNameAndSeasonId(name, season.getId());
        Augment augment = null;
        if (augmentOptional.isEmpty()) {
            augment = Augment.builder()
                    .name(name)
                    .season(season)
                    .build();
            augmentRepository.save(augment);
        } else {
            augment = augmentOptional.get();
        }
        return augment;
    }
}
