package by.klevitov.synctaskscheduler.taskscheduler.listener;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskSchedulerStartupListenerTest {
    private TaskSchedulerStartupListener taskSchedulerStartupListener;
    private SchedulerService mockedSchedulerService;
    private TaskService mockedTaskService;

    @BeforeEach
    public void setUp() {
        mockedSchedulerService = Mockito.mock(SchedulerService.class);
        mockedTaskService = Mockito.mock(TaskService.class);
        taskSchedulerStartupListener = new TaskSchedulerStartupListener(mockedSchedulerService, mockedTaskService);
    }

    @Test
    public void test_onApplicationEvent() {
        when(mockedTaskService.findByStatus(TaskStatus.ACTIVE))
                .thenReturn(List.of(new Task(), new Task()));
        when(mockedSchedulerService.scheduleTask(any(Task.class)))
                .then(invocationOnMock -> null);
        taskSchedulerStartupListener.onApplicationEvent(null);
        verify(mockedTaskService, times(1)).findByStatus(TaskStatus.ACTIVE);
        verify(mockedSchedulerService, times(2)).scheduleTask(any(Task.class));
    }
}
