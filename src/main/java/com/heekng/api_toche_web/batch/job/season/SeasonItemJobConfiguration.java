package com.heekng.api_toche_web.batch.job.season;

import com.heekng.api_toche_web.batch.chunk.processor.SeasonItemProcessor;
import com.heekng.api_toche_web.batch.chunk.writer.JpaItemListWriter;
import com.heekng.api_toche_web.entity.*;
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
public class SeasonItemJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final SeasonItemProcessor seasonItemProcessor;

    @Bean
    public Job seasonItemJob() {
        return jobBuilderFactory.get("seasonItemJob")
                .start(seasonItemStep())
                .build();
    }

    public Step seasonItemStep() {
        return stepBuilderFactory.get("seasonItemStep")
                .<Season, List<SeasonItem>>chunk(10)
                .reader(seasonItemReader())
                .processor(seasonItemProcessor)
                .writer(seasonItemWriter())
                .build();
    }


    private ItemReader<Season> seasonItemReader() {
        return new JpaPagingItemReaderBuilder<Season>()
                .name("seasonItemReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select s from Season s")
                .build();
    }

    private ItemWriter<List<SeasonItem>> seasonItemWriter() {
        JpaItemWriter<SeasonItem> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        writer.setUsePersist(false);

        return new JpaItemListWriter<>(writer);
    }

}
