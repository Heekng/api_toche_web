package com.heekng.api_toche_web.batch.job.match;

import com.heekng.api_toche_web.batch.chunk.processor.ApiMatchListProcessor;
import com.heekng.api_toche_web.batch.chunk.writer.JpaItemListWriter;
import com.heekng.api_toche_web.entity.Summoner;
import com.heekng.api_toche_web.entity.TftMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiMatchListJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final ApiMatchListProcessor apiMatchListProcessor;

    @Bean
    public Job apiMatchListJob() {
        return jobBuilderFactory.get("apiMatchListJob")
                .start(apiMatchListStep())
                .build();
    }

    public Step apiMatchListStep() {
        return stepBuilderFactory.get("apiMatchListStep")
                .<Summoner, List<TftMatch>>chunk(10)
                .reader(apiMatchListReader())
                .processor(apiMatchListProcessor)
                .writer(apiMatchListWriter())
                .build();
    }

    public JpaItemListWriter<TftMatch> apiMatchListWriter() {
        JpaItemWriter<TftMatch> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);

        return new JpaItemListWriter<>(writer);
    }


    public ItemReader<? extends Summoner> apiMatchListReader() {
        return new JpaPagingItemReaderBuilder<Summoner>()
                .name("apiSummonerDetailReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select s from Summoner s where s.puuid IS NOT NULL")
                .build();
    }
}
