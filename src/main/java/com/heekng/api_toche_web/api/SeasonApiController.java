package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.SeasonDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class SeasonApiController {

    private final SeasonRepository seasonRepository;

    @GetMapping("/seasons")
    public List<SeasonDTO.SeasonsResponse> seasons(
            @ModelAttribute SeasonDTO.SeasonsRequest seasonsRequest
    ) {
        List<Season> seasons = seasonRepository.searchBySeasonsRequest(seasonsRequest);
        return seasons.stream()
                .map(SeasonDTO.SeasonsResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/seasons/{seasonId}")
    public SeasonDTO.SeasonsResponse seasonListBySeasonNum(
            @PathVariable("seasonId") Long seasonId
    ) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Season 입니다."));
        return new SeasonDTO.SeasonsResponse(season);
    }
}
