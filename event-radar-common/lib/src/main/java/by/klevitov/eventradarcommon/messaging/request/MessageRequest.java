package by.klevitov.eventradarcommon.messaging.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static by.klevitov.eventradarcommon.util.IdGenerator.generateId;
import static java.time.LocalDateTime.now;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "id")
public class MessageRequest {
    @Builder.Default
    private String id = generateId();
    @Builder.Default
    private LocalDateTime createdDate = now();
    private RequestType requestType;
    private EntityType entityType;
    private EntityData entityData;
}
