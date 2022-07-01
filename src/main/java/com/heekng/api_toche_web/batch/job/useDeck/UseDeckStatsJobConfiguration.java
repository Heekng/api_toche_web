package com.heekng.api_toche_web.batch.job.useDeck;

import com.heekng.api_toche_web.batch.chunk.processor.UseDeckStatsProcessor;
import com.heekng.api_toche_web.entity.MatchInfo;
import com.heekng.api_toche_web.entity.Summoner;
import com.heekng.api_toche_web.entity.UseDeckAugment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class UseDeckStatsJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final UseDeckStatsProcessor useDeckStatsProcessor;

    @Bean
    public Job useDeckStatsJob() {
        return jobBuilderFactory.get("useDeckStatsJob")
                .start(useDeckStatsStep())
                .build();
    }

    private Step useDeckStatsStep() {
        return stepBuilderFactory.get("useDeckStatsStep")
                .<MatchInfo, MatchInfo>chunk(10)
                .reader(useDeckStatsReader())
                .processor(useDeckStatsProcessor)
                .writer(useDeckStatsWriter())
                .build();
    }

    private ItemWriter<MatchInfo> useDeckStatsWriter() {
        return new JpaItemWriterBuilder<MatchInfo>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

    private ItemReader<MatchInfo> useDeckStatsReader() {
        return new JpaPagingItemReaderBuilder<MatchInfo>()
                .name("useDeckStatsReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select m from MatchInfo m where m.isDeckCollected = FALSE")
                .build();
    }

}
