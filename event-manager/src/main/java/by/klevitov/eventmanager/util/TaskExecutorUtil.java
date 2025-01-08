package by.klevitov.eventmanager.util;

import by.klevitov.eventmanager.constant.ManagerExceptionMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class TaskExecutorUtil {
    private TaskExecutorUtil() {
    }

    public static void logException(final Exception e, final String taskName) {
        final String exceptionMessage = String.format(ManagerExceptionMessage.TASK_EXECUTION_ERROR, taskName, e.getMessage());
        log.error(exceptionMessage, e);
    }
}
