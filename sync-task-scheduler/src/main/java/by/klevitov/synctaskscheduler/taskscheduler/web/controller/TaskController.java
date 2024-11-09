package by.klevitov.synctaskscheduler.taskscheduler.web.controller;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.service.TaskService;
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
    private final TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public Task create(@RequestBody final Task task) {
        return service.create(task);
    }

    @PostMapping("/multiple")
    public List<Task> create(@RequestBody final List<Task> tasks) {
        return service.create(tasks);
    }

    @GetMapping
    public List<Task> findAll() {
        return service.findAll();
    }

    @PostMapping("/search")
    public List<Task> findByFields(@RequestBody final Map<String, Object> fields) {
        return service.findByFields(fields);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable final long id) {
        return service.findById(id);
    }

    @PutMapping
    public Task update(@RequestBody final Task task) {
        return service.update(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
