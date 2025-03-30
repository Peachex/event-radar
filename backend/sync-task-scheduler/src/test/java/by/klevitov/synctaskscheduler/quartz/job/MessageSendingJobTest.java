package by.klevitov.synctaskscheduler.quartz.job;

import by.klevitov.eventradarcommon.messaging.TaskSchedulerMessage;
import by.klevitov.synctaskscheduler.producer.TaskSchedulerProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static by.klevitov.synctaskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.PRODUCER_KEY;
import static by.klevitov.synctaskscheduler.quartz.creator.impl.QuartzEntityCreatorImpl.TASK_ID_TO_EXECUTE_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageSendingJobTest {
    private Job messageSendingJob;
    private JobExecutionContext mockedJobExecutionContext;

    @BeforeEach
    public void setUp() {
        messageSendingJob = new MessageSendingJob();
        mockedJobExecutionContext = Mockito.mock(JobExecutionContext.class);
    }

    @Test
    public void test_execute() throws JobExecutionException {
        TaskSchedulerProducer mockedProducer = Mockito.mock(TaskSchedulerProducer.class);
        JobDataMap mockedJobDataMap = Mockito.mock(JobDataMap.class);
        JobDetail mockedJobDetail = Mockito.mock(JobDetail.class);

        when(mockedJobExecutionContext.getJobDetail())
                .thenReturn(mockedJobDetail);
        when(mockedJobExecutionContext.getJobDetail().getJobDataMap())
                .thenReturn(mockedJobDataMap);
        when(mockedJobDataMap.get(PRODUCER_KEY))
                .thenReturn(mockedProducer);
        when(mockedJobDataMap.getString(TASK_ID_TO_EXECUTE_KEY))
                .thenReturn("taskIdToExecute");

        messageSendingJob.execute(mockedJobExecutionContext);

        verify(mockedProducer, times(1)).send(any(TaskSchedulerMessage.class));
    }
}
