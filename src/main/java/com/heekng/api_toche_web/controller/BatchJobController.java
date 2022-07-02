package com.heekng.api_toche_web.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BatchJobController {

    private final Job useDeckStatsJob;
    private final JobLauncher jobLauncher;

    @SneakyThrows
    @GetMapping("/batch/job/UseDeckStatsJob")
    public void startUseDeckStatsJob() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(useDeckStatsJob, jobParameters);

    }
}
