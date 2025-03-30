package by.klevitov.synctaskscheduler.service;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import by.klevitov.synctaskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.entity.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Task create(final Task task);

    List<Task> create(final List<Task> tasks);

    Task findById(final long id);

    List<Task> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch);

    Page<Task> findByFields(final Map<String, Object> fields, final boolean isCombinedMatch,
                            final PageRequestDTO pageRequestDTO);

    List<Task> findAll();

    Page<Task> findAll(final PageRequestDTO pageRequestDTO);

    List<Task> findByStatus(final TaskStatus status);

    Task update(final Task updatedTask);

    Task updateStatus(final long id, final TaskStatus updatedStatus);

    void delete(final long id);
}
