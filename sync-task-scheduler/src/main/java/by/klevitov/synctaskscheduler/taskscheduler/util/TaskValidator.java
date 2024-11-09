package by.klevitov.synctaskscheduler.taskscheduler.util;

import by.klevitov.synctaskscheduler.taskscheduler.entity.Task;
import by.klevitov.synctaskscheduler.taskscheduler.entity.TaskStatus;
import by.klevitov.synctaskscheduler.taskscheduler.exception.TaskValidatorException;
import lombok.extern.log4j.Log4j2;

import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.INVALID_CRON_EXPRESSION;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.NULL_OR_EMPTY_CRON_EXPRESSION;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.NULL_OR_EMPTY_TASK_ID;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.NULL_OR_EMPTY_TASK_ID_TO_EXECUTE;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.NULL_TASK;
import static by.klevitov.synctaskscheduler.taskscheduler.constant.TaskSchedulerExceptionMessage.REQUIRED_TASK_FIELDS_ARE_EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.quartz.CronExpression.isValidExpression;

@Log4j2
public final class TaskValidator {
    private TaskValidator() {
    }

    public static void validateTaskBeforeCreation(final Task task) {
        throwExceptionInCaseOfNullTask(task);
        throwExceptionInCaseOfEmptyTaskName(task.getName());
        throwExceptionInCaseOfEmptyTaskIdToExecute(task.getTaskIdToExecute());
        validateCronExpressionBeforeTaskCreation(task.getCronExpression());
        task.setId(0);
        task.setStatus(TaskStatus.ACTIVE);
    }

    private static void throwExceptionInCaseOfNullTask(final Task task) {
        if (task == null) {
            log.error(NULL_TASK);
            throw new TaskValidatorException(NULL_TASK);
        }
    }

    private static void throwExceptionInCaseOfEmptyTaskName(final String name) {
        if (isEmpty(name)) {
            log.error(NULL_OR_EMPTY_TASK_ID);
            throw new TaskValidatorException(NULL_OR_EMPTY_TASK_ID);
        }
    }

    private static void throwExceptionInCaseOfEmptyTaskIdToExecute(final String taskIdToExecute) {
        if (isEmpty(taskIdToExecute)) {
            log.error(NULL_OR_EMPTY_TASK_ID_TO_EXECUTE);
            throw new TaskValidatorException(NULL_OR_EMPTY_TASK_ID_TO_EXECUTE);
        }
    }

    private static void validateCronExpressionBeforeTaskCreation(final String cronExpression) {
        throwExceptionInCaseOfEmptyCronExpression(cronExpression);
        throwExceptionInCaseOfInvalidCronExpression(cronExpression);
    }

    private static void throwExceptionInCaseOfEmptyCronExpression(final String cronExpression) {
        if (isEmpty(cronExpression)) {
            log.error(NULL_OR_EMPTY_CRON_EXPRESSION);
            throw new TaskValidatorException(NULL_OR_EMPTY_CRON_EXPRESSION);
        }
    }

    public static void validateTaskBeforeUpdating(final Task task) {
        throwExceptionInCaseOfNullTask(task);
        throwExceptionInCaseOfTaskFieldsAreEmpty(task);
        validateCronExpressionBeforeTaskUpdating(task.getCronExpression());
        task.setStatus(null);
    }

    private static void throwExceptionInCaseOfTaskFieldsAreEmpty(final Task task) {
        if (isEmpty(task.getName()) && isEmpty(task.getDescription()) && isEmpty(task.getTaskIdToExecute())
                && isEmpty(task.getCronExpression())) {
            log.error(REQUIRED_TASK_FIELDS_ARE_EMPTY);
            throw new TaskValidatorException(REQUIRED_TASK_FIELDS_ARE_EMPTY);
        }
    }

    private static void validateCronExpressionBeforeTaskUpdating(final String cronExpression) {
        if (isNotEmpty(cronExpression)) {
            throwExceptionInCaseOfInvalidCronExpression(cronExpression);
        }
    }

    private static void throwExceptionInCaseOfInvalidCronExpression(final String cronExpression) {
        if (cronExpressionIsNotValid(cronExpression)) {
            String exceptionMessage = String.format(INVALID_CRON_EXPRESSION, cronExpression);
            log.error(exceptionMessage);
            throw new TaskValidatorException(exceptionMessage);
        }
    }

    private static boolean cronExpressionIsNotValid(final String cronExpression) {
        return !isValidExpression(cronExpression);
    }
}
