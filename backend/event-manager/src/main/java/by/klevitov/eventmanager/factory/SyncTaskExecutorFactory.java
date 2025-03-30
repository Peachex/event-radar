package by.klevitov.eventmanager.factory;

import by.klevitov.eventmanager.exception.SyncTaskExecutorFactoryException;
import by.klevitov.eventmanager.executor.SyncTaskExecutor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventmanager.constant.ManagerExceptionMessage.TASK_EXECUTOR_NOT_FOUND;

@Log4j2
@Getter
@Component
public class SyncTaskExecutorFactory {
    private final Map<String, SyncTaskExecutor> taskExecutorsMapWithKey;

    @Autowired
    public SyncTaskExecutorFactory(List<SyncTaskExecutor> taskExecutors) {
        this.taskExecutorsMapWithKey = new HashMap<>();
        for (SyncTaskExecutor taskExecutor : taskExecutors) {
            taskExecutorsMapWithKey.put(taskExecutor.retrieveExecutorId(), taskExecutor);
        }
    }

    public SyncTaskExecutor getTaskExecutor(final String taskIdToExecute) {
        SyncTaskExecutor taskExecutor = taskExecutorsMapWithKey.get(taskIdToExecute);
        throwExceptionInCaseOfExecutorNotFound(taskExecutor, taskIdToExecute);
        return taskExecutorsMapWithKey.get(taskIdToExecute);
    }

    private void throwExceptionInCaseOfExecutorNotFound(final SyncTaskExecutor executor, final String taskIdToExecute) {
        if (executor == null) {
            final String exceptionMessage = String.format(TASK_EXECUTOR_NOT_FOUND, taskIdToExecute);
            log.error(exceptionMessage);
            throw new SyncTaskExecutorFactoryException(exceptionMessage);
        }
    }
}
