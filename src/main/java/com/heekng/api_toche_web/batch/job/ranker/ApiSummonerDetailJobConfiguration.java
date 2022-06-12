package com.heekng.api_toche_web.batch.job.ranker;

import com.heekng.api_toche_web.batch.chunk.processor.ApiSummonerDetailProcessor;
import com.heekng.api_toche_web.entity.Summoner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiSummonerDetailJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final ApiSummonerDetailProcessor apiSummonerDetailProcessor;

    @Bean
    public Job apiSummonerDetailJob() {
        return jobBuilderFactory.get("apiSummonerDetailJob")
                .start(apiSummonerDetailStep())
                .build();
    }

    @Bean
    public Step apiSummonerDetailStep() {
        return stepBuilderFactory.get("apiSummonerDetailStep")
                .<Summoner, Summoner>chunk(10)
                .reader(apiSummonerDetailReader())
                .processor(apiSummonerDetailProcessor)
                .writer(apiSummonerDetailWriter())
                .build();
    }

    private ItemReader<? extends Summoner> apiSummonerDetailReader() {
        return new JpaPagingItemReaderBuilder<Summoner>()
                .name("apiSummonerDetailReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select s from Summoner s")
                .build();
    }

    private ItemWriter<? super Summoner> apiSummonerDetailWriter() {
        return new JpaItemWriterBuilder<Summoner>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

}
