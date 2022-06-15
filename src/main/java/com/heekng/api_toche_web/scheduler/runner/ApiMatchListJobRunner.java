package com.heekng.api_toche_web.scheduler.runner;

import com.heekng.api_toche_web.scheduler.schedule.ApiMatchListSchJob;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class ApiMatchListJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetailMaster = buildJobDetail(ApiMatchListSchJob.class, "ApiMatchListSchJob", "batch", new HashMap<>());
        Trigger triggerMaster = buildJobTrigger("0 10 * * * ?");

        try {
            scheduler.scheduleJob(jobDetailMaster, triggerMaster);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
