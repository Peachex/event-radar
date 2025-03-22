package by.klevitov.eventmanager.service;

import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskManagerService {
    void executeTask(final String taskIdToExecute);

    List<String> retrieveTaskExecutorIds();

    Page<String> retrieveTaskExecutorIds(final PageRequestDTO pageRequestDTO);
}
