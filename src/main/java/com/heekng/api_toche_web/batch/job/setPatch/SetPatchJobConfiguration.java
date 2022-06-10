package com.heekng.api_toche_web.batch.job.setPatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SetPatchJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Job itemInsertJob;
    private final Job traitInsertJob;
    private final Job unitInsertJob;

    @Bean
    public Job setPatchJob() {
        return jobBuilderFactory.get("setPatchJob")
                .start(itemInsertJobStep())
                .next(traitInsertJobStep())
                .next(unitInsertJobStep())
                .build();
    }

    @Bean
    public Step itemInsertJobStep() {
        return stepBuilderFactory.get("itemInsertJobStep")
                .job(itemInsertJob)
//                .parametersExtractor(jobParametersExtractor())
                .build();
    }

    @Bean
    public Step traitInsertJobStep() {
        return stepBuilderFactory.get("traitInsertJobStep")
                .job(traitInsertJob)
//                .parametersExtractor(jobParametersExtractor())
                .build();
    }

    @Bean
    public Step unitInsertJobStep() {
        return stepBuilderFactory.get("unitInsertJobStep")
                .job(unitInsertJob)
//                .parametersExtractor(jobParametersExtractor())
                .build();
    }

    private JobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"season"});
        return extractor;
    }


}
