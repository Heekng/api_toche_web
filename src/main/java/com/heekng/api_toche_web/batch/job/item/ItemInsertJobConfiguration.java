package com.heekng.api_toche_web.batch.job.item;

import com.heekng.api_toche_web.batch.chunk.processor.ItemInsertProcessor;
import com.heekng.api_toche_web.batch.domain.ItemInsertVO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class ItemInsertJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final SeasonRepository seasonRepository;

    @Bean
    public Job itemInsertJob() {
        return jobBuilderFactory.get("itemInsertJob")
                .start(itemInsertStep())
                .build();
    }

    @Bean
    public Step itemInsertStep() {
        return stepBuilderFactory.get("itemInsertStep")
                .<ItemInsertVO, Item>chunk(10)
                .reader(itemInsertReader())
                .processor(itemInsertProcessor(null))
                .writer(itemInsertWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends ItemInsertVO> itemInsertReader() {
        return new JsonItemReaderBuilder<ItemInsertVO>()
                .name("jsonItemInsertReader")
                .resource(new ClassPathResource("set5patch1115/items.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(ItemInsertVO.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<? super ItemInsertVO, ? extends Item> itemInsertProcessor(@Value("#{jobParameters[seasonNum]}") String seasonNum) {
        Season season = seasonRepository.findBySeasonNum(seasonNum).orElseThrow(() -> new IllegalStateException("존재하지 않는 Season입니다."));
        return new ItemInsertProcessor(season);
    }

    @Bean
    public ItemWriter<Item> itemInsertWriter() {
        return new JpaItemWriterBuilder<Item>()
                .entityManagerFactory(emf)
                .usePersist(true)
                .build();
    }

}
