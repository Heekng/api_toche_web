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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UseDeckStatsJobConfiguration {

    private final int poolSize = 10;
    private final int chunkSize = 10;

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
                .<MatchInfo, MatchInfo>chunk(chunkSize)
                .reader(useDeckStatsReader())
                .processor(useDeckStatsProcessor)
                .writer(useDeckStatsWriter())
                .taskExecutor(executor())
                .throttleLimit(poolSize)
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
                .pageSize(chunkSize)
                .queryString("select m from MatchInfo m where m.isDeckCollected = FALSE")
                .saveState(false)
                .build();
    }

    private TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

}
