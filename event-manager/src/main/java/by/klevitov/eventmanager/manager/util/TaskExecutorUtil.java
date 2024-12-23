package by.klevitov.eventmanager.manager.util;

import lombok.extern.log4j.Log4j2;

import static by.klevitov.eventmanager.manager.constant.ManagerExceptionMessage.TASK_EXECUTION_ERROR;

@Log4j2
public final class TaskExecutorUtil {
    private TaskExecutorUtil() {
    }

    public static void logException(final Exception e, final String taskName) {
        final String exceptionMessage = String.format(TASK_EXECUTION_ERROR, taskName, e.getMessage());
        log.error(exceptionMessage, e);
    }
}
