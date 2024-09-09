package by.klevitov.eventpersistor.messaging.comnon.response.dto;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String id;
    private String requestId;
    private LocalDateTime requestCreatedDate;
    private LocalDateTime responseCreatedDate;
    private boolean hasError;
    private EntityData entityData;

    public MessageResponse(String id, boolean hasError) {
        this.id = id;
        this.responseCreatedDate = now();
        this.hasError = hasError;
    }

    public MessageResponse(String id, String requestId, LocalDateTime requestCreatedDate, EntityData entityData) {
        this(id, entityData);
        this.requestId = requestId;
        this.responseCreatedDate = requestCreatedDate;
    }

    public MessageResponse(String id, EntityData entityData) {
        this.id = id;
        this.responseCreatedDate = requestCreatedDate;
        this.responseCreatedDate = now();
        this.hasError = false;
        this.entityData = entityData;
    }
}
