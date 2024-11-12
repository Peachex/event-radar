package by.klevitov.synctaskscheduler.taskscheduler.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public class MessageSendingJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String taskId = dataMap.getString("taskId");
        System.out.println("[" + LocalDateTime.now() + "] The task with id=" + taskId + " has been triggered."
                + "Job id=" + jobExecutionContext.getJobDetail().getKey());
    }

    //todo Add message broker logic.
}
