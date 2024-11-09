package by.klevitov.synctaskscheduler.test;

import org.springframework.stereotype.Component;

@Component
public class QuartzJobInitializer {
    //todo Delete this class.
//    @Autowired
//    private Scheduler scheduler;
//
//    @EventListener
//    public void onApplicationEvent(ContextRefreshedEvent event) throws SchedulerException {
//        JobDetail syncJob = JobBuilder.newJob(TestJob.class)
//                .withIdentity("syncJob", "sync")
//                .usingJobData("taskId", "new-events-uploading-job")
//                .build();
//
//        Trigger syncJobTrigger = TriggerBuilder.newTrigger()
//                .withIdentity("syncJobTrigger", "sync")
//                .startNow()
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInMinutes(1)
//                        .repeatForever())
//                .build();
//
//        JobDetail cleanJob = JobBuilder.newJob(TestJob.class)
//                .withIdentity("cleanJob", "sync")
//                .usingJobData("taskId", "existing-events-cleaning-job")
//                .build();
//
//        Trigger cleanJobTrigger = TriggerBuilder.newTrigger()
//                .withIdentity("cleanJobTrigger", "sync")
//                .startNow()
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInMinutes(2)
//                        .repeatForever())
//                .build();
//
////        scheduler.scheduleJob(syncJob, syncJobTrigger);
////        scheduler.scheduleJob(cleanJob, cleanJobTrigger);
//    }
}
