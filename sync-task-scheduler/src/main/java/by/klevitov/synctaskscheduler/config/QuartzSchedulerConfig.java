package by.klevitov.synctaskscheduler.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzSchedulerConfig {
    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public SchedulerFactory schedulerFactory() {
        return new StdSchedulerFactory();
    }
}
