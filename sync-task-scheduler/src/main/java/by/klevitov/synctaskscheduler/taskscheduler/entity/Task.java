package by.klevitov.synctaskscheduler.taskscheduler.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    @Column(nullable = false)
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
}
