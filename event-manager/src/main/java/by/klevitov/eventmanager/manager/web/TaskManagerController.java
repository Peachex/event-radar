package by.klevitov.eventmanager.manager.web;

import by.klevitov.eventmanager.manager.service.TaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskManagerController {
    private final TaskManagerService taskManagerService;

    @Autowired
    public TaskManagerController(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @GetMapping
    public List<String> findAllTasks() {
        return taskManagerService.retrieveTaskExecutorIds();
    }
}
