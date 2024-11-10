package by.klevitov.synctaskscheduler.taskscheduler.listener;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskSchedulerStartupListener {
    private final SchedulerService schedulerService;
    private final TaskService taskService;

    public TaskSchedulerStartupListener(SchedulerService schedulerService, TaskService taskService) {
        this.schedulerService = schedulerService;
        this.taskService = taskService;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Task> activeTasks = taskService.findByStatus(TaskStatus.ACTIVE);
        activeTasks.forEach(schedulerService::scheduleTask);
    }
}
