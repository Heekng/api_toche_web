package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.entity.Summoner;
import com.heekng.api_toche_web.entity.TftMatch;
import com.heekng.api_toche_web.repository.TftMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        List<String> matchList = restTemplate.getForObject(uri, List.class);

        log.info(matchList.toString());

        return matchList.stream()
                .filter(matchId -> !tftMatchRepository.existsByMatchId(matchId))
                .map(matchId -> TftMatch.builder()
                        .matchId(matchId)
                        .summoner(summoner)
                        .build())
                .collect(Collectors.toList());
    }
}
