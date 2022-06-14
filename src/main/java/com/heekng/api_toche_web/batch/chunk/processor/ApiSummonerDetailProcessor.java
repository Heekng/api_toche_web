package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.SummonerDetailDTO;
import com.heekng.api_toche_web.entity.Summoner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class ApiSummonerDetailProcessor implements ItemProcessor<Summoner, Summoner> {

    @Value("${riotApi.key}")
    private String RIOTAPI_KEY;
    @Value("${riotApi.path.summonerDetail}")
    private String RIOTAPI_PATH_SUMMONERDETAIL;

    @Override
    public Summoner process(Summoner summoner) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(RIOTAPI_PATH_SUMMONERDETAIL);
        builder.append(summoner.getRiotSummonerId());
        builder.append("?");
        builder.append("api_key=");
        builder.append(RIOTAPI_KEY);
        String uri = builder.toString();

        RestTemplate restTemplate = new RestTemplate();
        SummonerDetailDTO summonerDetailDTO = null;

        try {
            summonerDetailDTO = restTemplate.getForObject(uri, SummonerDetailDTO.class);
        } catch (HttpClientErrorException e) {
            String message = e.getMessage();
            if (message.contains("Rate limit exceeded")) {
                log.info("wait maximum request");
                Thread.sleep(120000);
            }
            summonerDetailDTO = restTemplate.getForObject(uri, SummonerDetailDTO.class);
        }

        summoner.updateBySummonerDetailDto(summonerDetailDTO);

        return summoner;
    }
}
