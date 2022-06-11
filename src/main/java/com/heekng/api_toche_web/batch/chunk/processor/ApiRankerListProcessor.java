package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.LeagueRankerDTO;
import com.heekng.api_toche_web.entity.Summoner;
import org.springframework.batch.item.ItemProcessor;


public class ApiRankerListProcessor implements ItemProcessor<LeagueRankerDTO, Summoner> {
    @Override
    public Summoner process(LeagueRankerDTO leagueRankerDTO) throws Exception {
        return leagueRankerDTO.toSummonerEntity();
    }
}
