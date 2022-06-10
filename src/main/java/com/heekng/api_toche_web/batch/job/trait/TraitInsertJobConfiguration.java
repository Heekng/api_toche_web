package com.heekng.api_toche_web.batch.job.trait;

import com.heekng.api_toche_web.batch.chunk.processor.ItemInsertProcessor;
import com.heekng.api_toche_web.batch.chunk.processor.TraitInsertProcessor;
import com.heekng.api_toche_web.batch.domain.ItemInsertVO;
import com.heekng.api_toche_web.batch.domain.TraitInsertVO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
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
public class TraitInsertJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final SeasonRepository seasonRepository;

    @Bean
    public Job traitInsertJob() {
        return jobBuilderFactory.get("traitInsertJob")
                .start(traitInsertStep())
                .build();
    }

    @Bean
    public Step traitInsertStep() {
        return stepBuilderFactory.get("traitInsertStep")
                .<TraitInsertVO, Trait>chunk(10)
                .reader(traitInsertReader())
                .processor(traitInsertProcessor(null))
                .writer(traitInsertWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends TraitInsertVO> traitInsertReader() {
        return new JsonItemReaderBuilder<TraitInsertVO>()
                .name("jsonTraitInsertReader")
                .resource(new ClassPathResource("defaultDatas/traits.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(TraitInsertVO.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<? super TraitInsertVO, ? extends Trait> traitInsertProcessor(@Value("#{jobParameters[seasonNum]}") String seasonNum) {
        Season season = seasonRepository.findBySeasonNum(seasonNum).orElseThrow(() -> new IllegalStateException("존재하지 않는 Season입니다."));
        return new TraitInsertProcessor(season);
    }

    @Bean
    public ItemWriter<Trait> traitInsertWriter() {
        return new JpaItemWriterBuilder<Trait>()
                .entityManagerFactory(emf)
                .build();
    }
}
