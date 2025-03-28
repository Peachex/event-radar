package by.klevitov.eventmanager.web.controller;

import by.klevitov.eventmanager.service.TaskManagerService;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.eventradarcommon.pagination.dto.PageResponseDTO;
import by.klevitov.eventradarcommon.pagination.dto.TaskIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static by.klevitov.eventradarcommon.pagination.dto.TaskIdDTO.createListFromTaskIds;

@RestController
@RequestMapping("tasks")
public class TaskManagerController {
    private final TaskManagerService taskManagerService;

    @Autowired
    public TaskManagerController(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @GetMapping
    public List<TaskIdDTO> findAllTasks() {
        List<String> taskIds = taskManagerService.retrieveTaskExecutorIds();
        return createListFromTaskIds(taskIds);
    }

    @PostMapping
    public PageResponseDTO<TaskIdDTO> findAllTasks(@RequestBody final PageRequestDTO pageRequestDTO) {
        Page<String> entityResultPage = taskManagerService.retrieveTaskExecutorIds(pageRequestDTO);
        List<String> taskIds = entityResultPage.getContent();
        return new PageResponseDTO<>(entityResultPage, createListFromTaskIds(taskIds));
    }
}
