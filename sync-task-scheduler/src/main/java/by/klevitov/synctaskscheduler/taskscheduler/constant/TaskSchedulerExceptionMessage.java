package by.klevitov.synctaskscheduler.taskscheduler.constant;

public final class TaskSchedulerExceptionMessage {
    public static final String NULL_TASK = "Task cannot be null.";
    public static final String NULL_OR_EMPTY_TASK_ID = "Task id cannot be null or empty.";
    public static final String REQUIRED_TASK_FIELDS_ARE_EMPTY = "Task fields cannot be null or empty.";
    public static final String NULL_OR_EMPTY_TASK_ID_TO_EXECUTE = "Task id to execute cannot be null or empty.";
    public static final String NULL_OR_EMPTY_CRON_EXPRESSION = "Cron expression = cannot be null or empty.";
    public static final String INVALID_CRON_EXPRESSION = "Cron expression is invalid: '%s'";
    public static final String TASK_NOT_FOUND = "Cannot find task with id: '%s'";
    public static final String TASK_ALREADY_EXISTS = "Task with name: '%s' already exists. Task id: '%s'.";

    private TaskSchedulerExceptionMessage() {
    }
}
