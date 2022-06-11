package com.heekng.api_toche_web.batch.job.ranker;

import com.heekng.api_toche_web.batch.chunk.processor.ApiRankerListProcessor;
import com.heekng.api_toche_web.batch.dto.LeagueListDTO;
import com.heekng.api_toche_web.batch.dto.LeagueRankerDTO;
import com.heekng.api_toche_web.entity.Summoner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiRankerListJobConfiguration {

    @Value("${riotApi.path.ranker}")
    private String RIOTAPI_PATH_RANKER;
    @Value("${riotApi.key}")
    private String RIOTAPI_KEY;

    private boolean checkRestCall = false;
    private LeagueListDTO apiResponseLeagueListDTO;
    private Integer apiResponseIndex = 0;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Bean
    public Job apiRankerListJob() {
        return jobBuilderFactory.get("apiRankerListJob")
                .start(apiRankerListStep())
                .next(apiRankerListResetStep())
                .build();
    }

    @Bean
    public Step apiRankerListStep() {
        return stepBuilderFactory.get("apiRankerListStep")
                .<LeagueRankerDTO, Summoner>chunk(10)
                .reader(apiRankerListReader(null))
                .processor(apiRankerListProcessor())
                .writer(apiRankerListWriter())
                .build();
    }

    @Bean
    public Step apiRankerListResetStep() {
        return stepBuilderFactory.get("apiRankerListResetStep")
                .tasklet((contribution, chunkContext) -> {
                    checkRestCall = false;
                    apiResponseLeagueListDTO = null;
                    apiResponseIndex = 0;
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public ItemWriter<? super Summoner> apiRankerListWriter() {
        return new JpaItemWriterBuilder<Summoner>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public ItemProcessor<? super LeagueRankerDTO, ? extends Summoner> apiRankerListProcessor() {
        return new ApiRankerListProcessor();
    }


    @Bean
    @StepScope
    public ItemReader<LeagueRankerDTO> apiRankerListReader(@Value("#{jobParameters[tierName]}") String tierName) {
        return new ItemReader<LeagueRankerDTO>() {
            @Override
            public LeagueRankerDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                if (!checkRestCall) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(RIOTAPI_PATH_RANKER);
                    builder.append(tierName);
                    builder.append("?");
                    builder.append("api_key=");
                    builder.append(RIOTAPI_KEY);
                    String uri = builder.toString();

                    RestTemplate restTemplate = new RestTemplate();
                    apiResponseLeagueListDTO = restTemplate.getForObject(uri, LeagueListDTO.class);

                    checkRestCall = true;
                }

                LeagueRankerDTO leagueRankerDTO = null;

                if (apiResponseIndex < apiResponseLeagueListDTO.getEntries().size()) {
                    leagueRankerDTO = apiResponseLeagueListDTO.getEntries().get(apiResponseIndex++);
                }

                return leagueRankerDTO;
            }
        };
    }
}
