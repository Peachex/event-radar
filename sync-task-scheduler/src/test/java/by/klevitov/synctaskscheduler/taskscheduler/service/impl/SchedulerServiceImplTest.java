package by.klevitov.synctaskscheduler.taskscheduler.service.impl;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.quartz.creator.QuartzEntityCreator;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.List;

import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.ACTIVE;
import static by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus.PAUSED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchedulerServiceImplTest {
    private SchedulerService schedulerService;
    private Scheduler mockedScheduler;
    private QuartzEntityCreator mockedEntityCreator;
    private TaskService mockedTaskService;

    @BeforeEach
    public void setUp() {
        mockedScheduler = mock(Scheduler.class);
        mockedEntityCreator = mock(QuartzEntityCreator.class);
        mockedTaskService = mock(TaskService.class);
        schedulerService = new SchedulerServiceImpl(mockedScheduler, mockedEntityCreator, mockedTaskService);
    }

    @Test
    public void test_scheduleTask_withNotPreviouslyScheduledActiveJob() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(false);
        when(mockedScheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
                .thenAnswer(invocationOnMock -> null);

        Task task = new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression");

        Task actual = schedulerService.scheduleTask(task);
        Task expected = new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                "cronExpression");
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(1)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(1)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(1)).checkExists((JobKey) any());
        verify(mockedScheduler, times(1)).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, never()).updateStatus(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void test_scheduleTask_withNotPreviouslyScheduledPausedJob() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(false);
        when(mockedScheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
                .thenAnswer(invocationOnMock -> null);
        when(mockedTaskService.updateStatus(anyLong(), any(TaskStatus.class)))
                .thenReturn(new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                        "cronExpression"));

        Task task = new Task(1, PAUSED, "taskName1", null, "taskIdToExecute", "cronExpression");

        Task actual = schedulerService.scheduleTask(task);
        Task expected = new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                "cronExpression");
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(1)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(1)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(1)).checkExists((JobKey) any());
        verify(mockedScheduler, times(1)).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, times(1)).updateStatus(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void test_scheduleTask_withPreviouslyScheduledJob() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(true);

        Task task = new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression");

        Task actual = schedulerService.scheduleTask(task);
        Task expected = new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute",
                "cronExpression");
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(1)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(1)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(1)).checkExists((JobKey) any());
        verify(mockedScheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, never()).updateStatus(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void test_scheduleTask_withNotPreviouslyScheduledActiveJobs() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(false);
        when(mockedScheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
                .thenAnswer(invocationOnMock -> null);

        List<Task> tasks = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );

        List<Task> actual = schedulerService.scheduleTask(tasks);
        List<Task> expected = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(3)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(3)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(3)).checkExists((JobKey) any());
        verify(mockedScheduler, times(3)).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, never()).updateStatus(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void test_scheduleTask_withNotPreviouslyScheduledActiveAndPausedJobs() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(false);
        when(mockedScheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
                .thenAnswer(invocationOnMock -> null);
        when(mockedTaskService.updateStatus(anyLong(), any(TaskStatus.class)))
                .thenReturn(new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute",
                        "cronExpression"))
                .thenReturn(new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute",
                        "cronExpression"));

        List<Task> tasks = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, PAUSED, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, PAUSED, "taskName3", null, "taskIdToExecute", "cronExpression")
        );

        List<Task> actual = schedulerService.scheduleTask(tasks);
        List<Task> expected = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(3)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(3)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(3)).checkExists((JobKey) any());
        verify(mockedScheduler, times(3)).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, times(2)).updateStatus(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void test_scheduleTask_withPreviouslyScheduledJobs() throws Exception {
        when(mockedEntityCreator.createJobDetail(any(Task.class)))
                .thenReturn(new JobDetailImpl());
        when(mockedEntityCreator.createTrigger(any(JobDetail.class), any(Task.class)))
                .thenReturn(new CronTriggerImpl());
        when(mockedScheduler.checkExists((JobKey) any()))
                .thenReturn(true);
        when(mockedScheduler.scheduleJob(any(JobDetail.class), any(Trigger.class)))
                .thenAnswer(invocationOnMock -> null);

        List<Task> tasks = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );

        List<Task> actual = schedulerService.scheduleTask(tasks);
        List<Task> expected = List.of(
                new Task(1, ACTIVE, "taskName1", null, "taskIdToExecute", "cronExpression"),
                new Task(2, ACTIVE, "taskName2", null, "taskIdToExecute", "cronExpression"),
                new Task(3, ACTIVE, "taskName3", null, "taskIdToExecute", "cronExpression")
        );
        assertEquals(expected, actual);

        verify(mockedEntityCreator, times(3)).createJobDetail(any(Task.class));
        verify(mockedEntityCreator, times(3)).createTrigger(any(JobDetail.class), any(Task.class));
        verify(mockedScheduler, times(3)).checkExists((JobKey) any());
        verify(mockedScheduler, never()).scheduleJob(any(JobDetail.class), any(Trigger.class));
        verify(mockedTaskService, never()).updateStatus(anyLong(), any(TaskStatus.class));
    }
}
