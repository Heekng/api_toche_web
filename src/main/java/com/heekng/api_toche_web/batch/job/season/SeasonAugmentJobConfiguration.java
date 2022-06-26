package com.heekng.api_toche_web.batch.job.season;

import com.heekng.api_toche_web.batch.chunk.processor.SeasonAugmentProcessor;
import com.heekng.api_toche_web.batch.chunk.writer.JpaItemListWriter;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.SeasonAugment;
import com.heekng.api_toche_web.entity.SeasonItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeasonAugmentJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final SeasonAugmentProcessor seasonAugmentProcessor;

    @Bean
    public Job seasonAugmentJob() {
        return jobBuilderFactory.get("seasonAugmentJob")
                .start(seasonAugmentStep())
                .build();
    }

    private Step seasonAugmentStep() {
        return stepBuilderFactory.get("seasonAugmentStep")
                .<Season, List<SeasonAugment>>chunk(10)
                .reader(seasonAugmentReader())
                .processor(seasonAugmentProcessor)
                .writer(seasonAugmentWriter())
                .build();
    }

    private ItemReader<Season> seasonAugmentReader() {
        return new JpaPagingItemReaderBuilder<Season>()
                .name("seasonItemReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select s from Season s")
                .build();
    }

    private ItemWriter<List<SeasonAugment>> seasonAugmentWriter() {
        JpaItemWriter<SeasonAugment> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        writer.setUsePersist(false);

        return new JpaItemListWriter<>(writer);
    }
}
