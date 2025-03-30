package by.klevitov.eventradarcommon.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static by.klevitov.eventradarcommon.util.IdGenerator.generateId;
import static java.time.LocalDateTime.now;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString
public class TaskSchedulerMessage {
    private final String id = generateId();
    private final LocalDateTime createdDate = now();
    private String taskIdToExecute;
}
