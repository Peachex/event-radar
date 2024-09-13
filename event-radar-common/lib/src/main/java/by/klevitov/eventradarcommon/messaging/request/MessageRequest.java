package by.klevitov.eventradarcommon.messaging.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import static by.klevitov.eventradarcommon.util.IdGenerator.generateId;
import static java.time.LocalDateTime.now;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest implements Serializable {
    @Builder.Default
    private String id = generateId();
    @Builder.Default
    private LocalDateTime createdDate = now();
    private RequestType requestType;
    private EntityType entityType;
    private EntityData entityData;
}
