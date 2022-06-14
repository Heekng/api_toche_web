package com.heekng.api_toche_web.scheduler.schedule;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ApiMatchListSchJob extends QuartzJobBean {

    private final Job apiMatchListJob;
    private final JobLauncher jobLauncher;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(apiMatchListJob, jobParameters);
    }
}
