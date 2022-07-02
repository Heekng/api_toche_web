package com.heekng.api_toche_web.scheduler.runner;

import com.heekng.api_toche_web.scheduler.schedule.SeasonMappingItemAndAugmentSchJob;
import com.heekng.api_toche_web.scheduler.schedule.UseDeckStatsSchJob;
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
public class UseDeckStatsJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetailMaster = buildJobDetail(UseDeckStatsSchJob.class, "UseDeckStatsSchJob", "batch", new HashMap<>());
        Trigger triggerMaster = buildJobTrigger("0 15 0/2 * * ?");

        try {
            scheduler.scheduleJob(jobDetailMaster, triggerMaster);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
