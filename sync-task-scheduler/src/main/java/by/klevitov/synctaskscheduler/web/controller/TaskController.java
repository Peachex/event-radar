package by.klevitov.synctaskscheduler.web.controller;

import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.service.SchedulerService;
import by.klevitov.synctaskscheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskService taskService;
    private final SchedulerService schedulerService;

    @Autowired
    public TaskController(TaskService taskService, SchedulerService schedulerService) {
        this.taskService = taskService;
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public Task create(@RequestBody final Task task) {
        Task createdTask = taskService.create(task);
        return schedulerService.scheduleTask(createdTask);
    }

    @PostMapping("/multiple")
    public List<Task> create(@RequestBody final List<Task> tasks) {
        List<Task> createdTasks = taskService.create(tasks);
        return schedulerService.scheduleTask(createdTasks);
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @PostMapping("/search")
    public List<Task> findByFields(@RequestBody final Map<String, Object> fields) {
        return taskService.findByFields(fields);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable final long id) {
        return taskService.findById(id);
    }

    @PutMapping
    public Task update(@RequestBody final Task task) {
        Task updatedTask = taskService.update(task);
        return schedulerService.rescheduleTask(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final long id) {
        Task taskToDelete = taskService.findById(id);
        schedulerService.deleteTask(taskToDelete);
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
