package by.klevitov.synctaskscheduler.taskscheduler.quartz.job;

import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import by.klevitov.synctaskscheduler.taskscheduler.producer.TaskSchedulerProducer;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.PRODUCER_KEY;
import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.TASK_ID_TO_EXECUTE_KEY;

public class MessageSendingJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TaskSchedulerProducer producer = (TaskSchedulerProducer) jobDataMap.get(PRODUCER_KEY);
        String taskIdToExecute = jobDataMap.getString(TASK_ID_TO_EXECUTE_KEY);
        TaskSchedulerMessage message = new TaskSchedulerMessage(taskIdToExecute);
        producer.send(message);
    }
}
