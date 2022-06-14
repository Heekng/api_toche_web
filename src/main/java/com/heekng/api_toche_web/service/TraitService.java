package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.repository.TraitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TraitService {

    private final TraitRepository traitRepository;

    @Transactional
    public Trait findOrSave(String name, Integer tierTotalCount, Season season) {
        Optional<Trait> traitOptional = traitRepository.findByNameAndSeasonId(name, season.getId());
        Trait trait = null;
        if (traitOptional.isEmpty()) {
            trait = Trait.builder()
                    .name(name)
                    .tierTotalCount(tierTotalCount)
                    .season(season)
                    .build();
            traitRepository.save(trait);
        } else {
            trait = traitOptional.get();
        }
        return trait;
    }
}
