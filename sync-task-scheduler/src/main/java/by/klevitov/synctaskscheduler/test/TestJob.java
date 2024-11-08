package by.klevitov.synctaskscheduler.test;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

@Log4j2
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String taskId = dataMap.getString("taskId");
        System.out.println("[" + LocalDateTime.now() + "] The task with id=" + taskId + " has been triggered.");
    }

    //todo Delete this class.
}
