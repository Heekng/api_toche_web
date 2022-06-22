package com.heekng.api_toche_web.scheduler.schedule;

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
public class ApiSummonerDetailSchJob extends QuartzJobBean {

    private final Job apiSummonerDetailJob;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 실행된 경우에는 재실행을 막는다.
        int jobInstanceCount = jobExplorer.getJobInstanceCount(apiSummonerDetailJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(apiSummonerDetailJob.getName(), 0, jobInstanceCount);

        if (jobInstances.size() > 0) {
            for (JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream()
                        .filter(jobExecution ->
                                jobExecution.getJobParameters().getString("runDate").equals(dateString)
                        ).collect(Collectors.toList());
                if (!jobExecutionList.isEmpty()) {
                    throw new JobExecutionException(dateString + " already exists");
                }
            }
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("runDate", dateString)
                .toJobParameters();

        jobLauncher.run(apiSummonerDetailJob, jobParameters);
    }
}
