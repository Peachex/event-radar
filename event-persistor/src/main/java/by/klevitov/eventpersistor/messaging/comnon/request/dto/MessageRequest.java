package by.klevitov.eventpersistor.messaging.comnon.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static by.klevitov.eventpersistor.messaging.util.IdGenerator.generateId;
import static java.time.LocalDateTime.now;

@Data
@Builder
public class MessageRequest {
    @Builder.Default
    private String id = generateId();
    @Builder.Default
    private LocalDateTime createdDate = now();
    private RequestType requestType;
    private EntityType entityType;
    private EntityData entityData;
}
