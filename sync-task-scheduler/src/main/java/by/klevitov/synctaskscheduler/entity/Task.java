package by.klevitov.synctaskscheduler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Entity(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString
public class Task {
    private static final String TASK_POSTFIX = "task";
    private static final String TRIGGER_POSTFIX = "trigger";
    private static final String IDENTITY_FORMAT = "%s_%s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private String taskIdToExecute;
    @Column(nullable = false)
    private String cronExpression;

    public void copyValuesForNullOrEmptyFieldsFromTask(Task source) {
        id = (id == 0 ? source.id : id);
        status = (status == null ? source.status : status);
        name = (isEmpty(name) ? source.name : name);
        description = (isEmpty(description) ? source.getDescription() : description);
        taskIdToExecute = (isEmpty(taskIdToExecute) ? source.taskIdToExecute : taskIdToExecute);
        cronExpression = (isEmpty(cronExpression) ? source.cronExpression : cronExpression);
    }

    public String createTaskIdentityName() {
        return String.format(IDENTITY_FORMAT, id, TASK_POSTFIX);
    }

    public String createTriggerIdentityName() {
        return String.format(IDENTITY_FORMAT, id, TRIGGER_POSTFIX);
    }
}
