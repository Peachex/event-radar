package by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.producer.TaskSchedulerProducer;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.QuartzEntityCreator;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.job.MessageSendingJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.PRODUCER_KEY;
import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.TASK_ID_TO_EXECUTE_KEY;
import static by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.TRIGGER_GROUP;
import static by.klevitov.synctaskscheduler.taskscheduler.util.SchedulerUtil.JOB_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class QuartzEntityCreatorImplTest {
    private QuartzEntityCreator creator;
    private TaskSchedulerProducer mockedProducer;
    private Task mockedTask;

    @BeforeEach
    public void setUp() {
        mockedProducer = Mockito.mock(TaskSchedulerProducer.class);
        creator = new QuartzEntityCreatorImpl(mockedProducer);
        mockedTask = Mockito.mock(Task.class);
    }

    @Test
    public void test_createJobDetail() {
        when(mockedTask.createTaskIdentityName())
                .thenReturn("taskIdentityName");
        when(mockedTask.getTaskIdToExecute())
                .thenReturn("taskIdToExecute");
        JobDetail expected = JobBuilder
                .newJob(MessageSendingJob.class)
                .withIdentity("taskIdentityName", JOB_GROUP)
                .usingJobData(createJobDataMap())
                .build();
        JobDetail actual = creator.createJobDetail(mockedTask);
        assertEquals(expected, actual);
    }

    private JobDataMap createJobDataMap() {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(PRODUCER_KEY, mockedProducer);
        jobDataMap.put(TASK_ID_TO_EXECUTE_KEY, "taskIdToExecute");
        return jobDataMap;
    }

    @Test
    public void test_createTrigger() {
        JobDetail mockedJobDetail = Mockito.mock(JobDetail.class);

        when(mockedJobDetail.getKey())
                .thenReturn(new JobKey("jobKey"));
        when(mockedTask.createTriggerIdentityName())
                .thenReturn("triggerIdentityName");
        when(mockedTask.getCronExpression())
                .thenReturn("0/3 * * * * ?");

        Trigger expected = TriggerBuilder.newTrigger()
                .withIdentity("triggerIdentityName", TRIGGER_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
                .forJob(mockedJobDetail)
                .build();
        Trigger actual = creator.createTrigger(mockedJobDetail, mockedTask);

        assertEquals(expected, actual);
    }
}
