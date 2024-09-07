package by.klevitov.eventpersistor.messaging.comnon.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageRequest {
    private String id;
    private RequestType requestType;
    private EntityType entityType;
    private EntityData entityData;
}
