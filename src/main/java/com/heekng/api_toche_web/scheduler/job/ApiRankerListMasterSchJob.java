package com.heekng.api_toche_web.scheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiRankerListMasterSchJob extends QuartzJobBean {

    private final Job apiRankerListJob;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final String TIER_NAME = "master";
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 실행된 경우에는 재실행을 막는다.
        int jobInstanceCount = jobExplorer.getJobInstanceCount(apiRankerListJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(apiRankerListJob.getName(), 0, jobInstanceCount);

        if (jobInstances.size() > 0) {
            for (JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream()
                        .filter(jobExecution ->
                                jobExecution.getJobParameters().getString("tierName").equals(TIER_NAME)
                        ).filter(jobExecution ->
                                jobExecution.getJobParameters().getString("runDate").equals(dateString)
                        ).collect(Collectors.toList());
                if (!jobExecutionList.isEmpty()) {
                    throw new JobExecutionException(TIER_NAME + ", " + dateString + " already exists");
                }
            }
        }

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        JobParameters jobParameters = jobParametersBuilder
                .addLong("id", new Date().getTime())
                .addString("runDate", dateString)
                .addString("tierName", TIER_NAME)
                .toJobParameters();
        jobLauncher.run(apiRankerListJob, jobParameters);
    }
}
