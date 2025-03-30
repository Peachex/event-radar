package by.klevitov.synctaskscheduler.constant;

public final class TaskSchedulerExceptionMessage {
    public static final String NULL_TASK = "Task cannot be null.";
    public static final String NULL_OR_EMPTY_TASK_NAME = "Task name cannot be null or empty.";
    public static final String REQUIRED_TASK_FIELDS_ARE_EMPTY = "Task fields cannot be null or empty.";
    public static final String NULL_OR_EMPTY_TASK_ID_TO_EXECUTE = "Task id to execute cannot be null or empty.";
    public static final String NULL_OR_EMPTY_CRON_EXPRESSION = "Cron expression = cannot be null or empty.";
    public static final String INVALID_CRON_EXPRESSION = "Cron expression is invalid: '%s'";
    public static final String NULL_TASK_STATUS = "Task status cannot be null.";
    public static final String TASK_NOT_FOUND = "Cannot find task with id: '%s'";
    public static final String TASK_ALREADY_EXISTS = "Task with name: '%s' already exists. Task id: '%s'.";
    public static final String SCHEDULING_JOB_ERROR = "There was some issue during job scheduling. Trigger key: '%s'.";
    public static final String RESCHEDULING_JOB_ERROR = "There was some issue during job rescheduling. Job key: '%s'.";
    public static final String PAUSING_JOB_ERROR = "There was some issue during job pausing. Job key: '%s'.";
    public static final String RESUMING_JOB_ERROR = "There was some issue during job resuming. Job key: '%s'.";
    public static final String DELETION_JOB_ERROR = "There was some issue during job deletion. Job key: '%s'.";
    public static final String TRIGGERING_JOB_ERROR = "There was some issue during job triggering. Job key: '%s'.";

    private TaskSchedulerExceptionMessage() {
    }
}
