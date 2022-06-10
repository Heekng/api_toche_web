package com.heekng.api_toche_web.batch.job.unit;

import com.heekng.api_toche_web.batch.chunk.processor.UnitInsertProcessor;
import com.heekng.api_toche_web.batch.domain.UnitInsertVO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.TraitRepository;
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
public class UnitInsertJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final SeasonRepository seasonRepository;
    private final TraitRepository traitRepository;

    @Bean
    public Job unitInsertJob() {
        return jobBuilderFactory.get("unitInsertJob")
                .start(unitInsertStep())
                .build();
    }

    @Bean
    public Step unitInsertStep() {
        return stepBuilderFactory.get("unitInsertStep")
                .<UnitInsertVO, Unit>chunk(10)
                .reader(unitInsertReader())
                .processor(unitInsertProcessor(null))
                .writer(unitInsertWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends UnitInsertVO> unitInsertReader() {
        return new JsonItemReaderBuilder<UnitInsertVO>()
                .name("jsonUnitInsertReader")
                .resource(new ClassPathResource("set5patch1115/champions.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(UnitInsertVO.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<? super UnitInsertVO, ? extends Unit> unitInsertProcessor(@Value("#{jobParameters[seasonNum]}") String seasonNum) {
        Season season = seasonRepository.findBySeasonNum(seasonNum).orElseThrow(() -> new IllegalStateException("존재하지 않는 Season입니다."));
        return new UnitInsertProcessor(season, traitRepository);
    }

    @Bean
    public ItemWriter<Unit> unitInsertWriter() {
        return new JpaItemWriterBuilder<Unit>()
                .entityManagerFactory(emf)
                .build();
    }
}
