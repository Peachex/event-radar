package by.klevitov.eventpersistor.messaging.comnon.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@AllArgsConstructor
@Builder
public class MessageRequest {
    private String id;
    @Builder.Default
    private LocalDateTime createdDate = now();
    private RequestType requestType;
    private EntityType entityType;
    private EntityData entityData;
}
