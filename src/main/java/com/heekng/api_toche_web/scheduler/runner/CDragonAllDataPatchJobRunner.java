package com.heekng.api_toche_web.scheduler.runner;

import com.heekng.api_toche_web.scheduler.schedule.ApiMatchDetailSchJob;
import com.heekng.api_toche_web.scheduler.schedule.CDragonAllDataPatchSchJob;
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
public class CDragonAllDataPatchJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetailMaster = buildJobDetail(CDragonAllDataPatchSchJob.class, "CDragonAllDataPatchSchJob", "batch", new HashMap<>());
        Trigger triggerMaster = buildJobTrigger("0 0 0 * * ?");

        try {
            scheduler.scheduleJob(jobDetailMaster, triggerMaster);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
