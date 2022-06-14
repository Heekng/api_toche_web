package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.MatchDetailDTO;
import com.heekng.api_toche_web.entity.Summoner;
import com.heekng.api_toche_web.entity.TftMatch;
import com.heekng.api_toche_web.repository.TftMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiMatchListProcessor implements ItemProcessor<Summoner, List<TftMatch>> {

    @Value("${riotApi.key}")
    private String RIOTAPI_KEY;
    @Value("${riotApi.path.matches}")
    private String RIOTAPI_PATH_MATCHES;
    private final TftMatchRepository tftMatchRepository;

    @Override
    public List<TftMatch> process(Summoner summoner) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(RIOTAPI_PATH_MATCHES);
        builder.append(summoner.getPuuid());
        builder.append("/ids");
        builder.append("?");
        builder.append("api_key=");
        builder.append(RIOTAPI_KEY);
        builder.append("&start=0");
        builder.append("&count=100");
        String uri = builder.toString();

        RestTemplate restTemplate = new RestTemplate();
        List<String> matchList = null;
        try {
            matchList = restTemplate.getForObject(uri, List.class);
        } catch (HttpClientErrorException e) {
            String message = e.getMessage();
            if (message.contains("Rate limit exceeded")) {
                log.info("wait maximum request");
                Thread.sleep(120000);
            }
            matchList = restTemplate.getForObject(uri, List.class);
        }

        List<TftMatch> tftMatchList = new ArrayList<>();

        for (String matchId : matchList) {
            Boolean existsByMatchId = tftMatchRepository.existsByMatchId(matchId);
            if (!existsByMatchId) {
                TftMatch tftMatch = TftMatch.builder()
                        .matchId(matchId)
                        .summoner(summoner)
                        .build();
                tftMatchList.add(tftMatch);
            } else {
                break;
            }
        }

        return tftMatchList;
    }
}
