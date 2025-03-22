package by.klevitov.eventmanager.service.impl;

import by.klevitov.eventmanager.constant.ManagerExceptionMessage;
import by.klevitov.eventmanager.exception.TaskManagerServiceException;
import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.factory.SyncTaskExecutorFactory;
import by.klevitov.eventmanager.service.TaskManagerService;
import by.klevitov.eventradarcommon.pagination.dto.PageRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static by.klevitov.eventradarcommon.pagination.util.PageRequestValidator.validatePageRequest;
import static by.klevitov.eventradarcommon.pagination.util.PaginationUtil.trimToPageSize;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
@Service
public class TaskManagerServiceImpl implements TaskManagerService {
    private final SyncTaskExecutorFactory taskRegistry;

    public TaskManagerServiceImpl(SyncTaskExecutorFactory taskRegistry) {
        this.taskRegistry = taskRegistry;
    }

    @Override
    public void executeTask(final String taskIdToExecute) {
        throwExceptionInCaseOfEmptyTaskIdToExecute(taskIdToExecute);
        SyncTaskExecutor taskExecutor = taskRegistry.getTaskExecutor(taskIdToExecute);
        executeTaskInSeparateThread(taskExecutor);
    }

    private void throwExceptionInCaseOfEmptyTaskIdToExecute(final String taskIdToExecute) {
        if (isEmpty(taskIdToExecute)) {
            log.error(ManagerExceptionMessage.NULL_OR_EMPTY_TASK_TO_EXECUTE);
            throw new TaskManagerServiceException(ManagerExceptionMessage.NULL_OR_EMPTY_TASK_TO_EXECUTE);
        }
    }

    private void executeTaskInSeparateThread(final SyncTaskExecutor taskExecutor) {
        Thread taskInSeparateThread = new Thread(taskExecutor::execute);
        taskInSeparateThread.start();
    }

    @Override
    public List<String> retrieveTaskExecutorIds() {
        return new ArrayList<>(taskRegistry.getTaskExecutorsMapWithKey().keySet());
    }

    @Override
    public Page<String> retrieveTaskExecutorIds(PageRequestDTO pageRequestDTO) {
        validatePageRequest(pageRequestDTO);
        List<String> foundIds = new ArrayList<>(taskRegistry.getTaskExecutorsMapWithKey().keySet());
        PageRequest pageRequest = pageRequestDTO.createPageRequest();
        long totalCount = foundIds.size();
        trimToPageSize(foundIds, pageRequest.getPageSize());
        return new PageImpl<>(foundIds, pageRequest, totalCount);
    }
}
