package com.heekng.api_toche_web.scheduler.runner;

import com.heekng.api_toche_web.scheduler.schedule.ApiMatchDetailSchJob;
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
public class ApiMatchDetailJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetailMaster = buildJobDetail(ApiMatchDetailSchJob.class, "ApiMatchDetailSchJob", "batch", new HashMap<>());
        Trigger triggerMaster = buildJobTrigger("0 20 * * * ?");

        try {
            scheduler.scheduleJob(jobDetailMaster, triggerMaster);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
