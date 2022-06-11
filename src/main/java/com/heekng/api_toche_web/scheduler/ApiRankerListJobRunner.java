package com.heekng.api_toche_web.scheduler;

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
public class ApiRankerListJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetailChallenger = buildJobDetail(ApiRankerListChallengerSchJob.class, "ApiRankerListChallengerJob", "batch", new HashMap<>());
        Trigger triggerChallenger = buildJobTrigger("0 13 * * * ?");

        JobDetail jobDetailGrandmaster = buildJobDetail(ApiRankerListGrandmasterSchJob.class, "ApiRankerListGrandmasterJob", "batch", new HashMap<>());
        Trigger triggerGrandmaster = buildJobTrigger("0 22 * * * ?");

        JobDetail jobDetailMaster = buildJobDetail(ApiRankerListMasterSchJob.class, "ApiRankerListMasterJob", "batch", new HashMap<>());
        Trigger triggerMaster = buildJobTrigger("0 15 * * * ?");

        try {
            scheduler.scheduleJob(jobDetailChallenger, triggerChallenger);
            scheduler.scheduleJob(jobDetailGrandmaster, triggerGrandmaster);
            scheduler.scheduleJob(jobDetailMaster, triggerMaster);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
