package com.heekng.api_toche_web.batch.job.season;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeasonMappingItemAndAugmentJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final Job seasonAugmentJob;
    private final Job seasonItemJob;

    @Bean
    public Job seasonMappingItemAndAugmentJob() {
        return jobBuilderFactory.get("seasonMappingItemAndAugmentJob")
                .start(seasonAugmentJobStep(null))
                .next(seasonItemJobStep(null))
                .build();
    }

    private Step seasonItemJobStep(JobLauncher jobLauncher) {
        return stepBuilderFactory.get("seasonItemJobStep")
                .job(seasonItemJob)
                .launcher(jobLauncher)
                .build();
    }

    private Step seasonAugmentJobStep(JobLauncher jobLauncher) {
        return stepBuilderFactory.get("seasonAugmentJobStep")
                .job(seasonAugmentJob)
                .launcher(jobLauncher)
                .build();
    }
}
