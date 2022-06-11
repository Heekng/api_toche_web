package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.LeagueRankerDTO;
import com.heekng.api_toche_web.entity.Summoner;
import com.heekng.api_toche_web.repository.SummonerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiRankerListProcessor implements ItemProcessor<LeagueRankerDTO, Summoner> {

    private final SummonerRepository summonerRepository;

    @Override
    public Summoner process(LeagueRankerDTO leagueRankerDTO) throws Exception {
        return summonerRepository.findByRiotSummonerId(leagueRankerDTO.getSummonerId())
                .orElse(leagueRankerDTO.toSummonerEntity())
                .updateRiotSummonerName(leagueRankerDTO.getSummonerName());
    }
}
