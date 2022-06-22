package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;

    @Transactional
    public Season findOrSave(Integer seasonNum, String seasonName) {
        Optional<Season> seasonOptional = seasonRepository.findBySeasonName(seasonName);
        Season season = null;
        if (seasonOptional.isEmpty()) {
            season = Season.builder()
                    .seasonName(seasonName)
                    .seasonNum(seasonNum)
                    .build();
            seasonRepository.save(season);
        } else {
            season = seasonOptional.get();
        }
        return season;
    }
}
