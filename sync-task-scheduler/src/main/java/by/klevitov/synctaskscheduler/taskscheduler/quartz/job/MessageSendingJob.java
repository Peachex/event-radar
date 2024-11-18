package by.klevitov.synctaskscheduler.taskscheduler.quartz.job;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.producer.TaskSchedulerProducer;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.PRODUCER_KEY;
import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.TASK_ID_KEY;

public class MessageSendingJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String taskId = jobDataMap.getString(TASK_ID_KEY);
        TaskSchedulerProducer producer = (TaskSchedulerProducer) jobDataMap.get(PRODUCER_KEY);
        String message = "[" + LocalDateTime.now() + "] The task with id=" + taskId + " has been triggered."
                + "Job id=" + jobExecutionContext.getJobDetail().getKey();
//        System.out.println(message);
        producer.send(new Task(12345L, TaskStatus.ACTIVE, "TestTask", "TestDescription", "TestTaskIdToExecute", "TestCronExpression"));
    }

    //todo Add message broker logic.
}
