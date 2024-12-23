package by.klevitov.eventmanager.manager.service.impl;

import by.klevitov.eventmanager.manager.exception.TaskManagerServiceException;
import by.klevitov.eventmanager.manager.executor.SyncTaskExecutor;
import by.klevitov.eventmanager.manager.factory.SyncTaskExecutorFactory;
import by.klevitov.eventmanager.manager.service.TaskManagerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static by.klevitov.eventmanager.manager.constant.ManagerExceptionMessage.NULL_OR_EMPTY_TASK_TO_EXECUTE;
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
        taskExecutor.execute();
    }

    private void throwExceptionInCaseOfEmptyTaskIdToExecute(final String taskIdToExecute) {
        if (isEmpty(taskIdToExecute)) {
            log.error(NULL_OR_EMPTY_TASK_TO_EXECUTE);
            throw new TaskManagerServiceException(NULL_OR_EMPTY_TASK_TO_EXECUTE);
        }
    }

    @Override
    public List<String> retrieveTaskExecutorIds() {
        return new ArrayList<>(taskRegistry.getTaskExecutorsMapWithKey().keySet());
    }
}
