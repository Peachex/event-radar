package by.klevitov.synctaskscheduler.taskscheduler.web.controller;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scheduler")
public class SchedulerController {
    private final SchedulerService schedulerService;
    private final TaskService taskService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService, TaskService taskService) {
        this.schedulerService = schedulerService;
        this.taskService = taskService;
    }

    @GetMapping("/pause/{taskId}")
    public Task pauseTask(@PathVariable long taskId) {
        Task task = taskService.findById(taskId);
        return schedulerService.pauseTask(task);
    }

    @GetMapping("/resume/{taskId}")
    public Task resumeTask(@PathVariable long taskId) {
        //fixme Check why does resumed task run 3 times before it get to normal schedule.
        Task task = taskService.findById(taskId);
        return schedulerService.resumeTask(task);
    }

    @GetMapping("/run/{taskId}")
    public void startTask(@PathVariable long taskId) {
        //fixme Fix issue with missing trigger. It might be fixed after scheduling tasks from database
        // when starting the server.
        Task task = taskService.findById(taskId);
        schedulerService.triggerTask(task);
    }
}
