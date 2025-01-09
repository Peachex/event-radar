package by.klevitov.synctaskscheduler.util;

import by.klevitov.synctaskscheduler.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobKey;

import static by.klevitov.synctaskscheduler.util.SchedulerUtil.JOB_GROUP;
import static by.klevitov.synctaskscheduler.util.SchedulerUtil.createJobKeyBasedOnTask;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchedulerUtilTest {
    private Task mockedTask;

    @BeforeEach
    public void setUp() {
        mockedTask = Mockito.mock(Task.class);
    }

    @Test
    public void test_createJobKeyBasedOnTask() {
        Mockito.when(mockedTask.createTaskIdentityName())
                .thenReturn("taskIdentityName");
        JobKey expected = JobKey.jobKey("taskIdentityName", JOB_GROUP);
        JobKey actual = createJobKeyBasedOnTask(mockedTask);
        assertEquals(expected, actual);
    }
}
