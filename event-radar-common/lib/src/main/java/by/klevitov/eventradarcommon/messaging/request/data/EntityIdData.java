package by.klevitov.eventradarcommon.messaging.request.data;

import by.klevitov.eventradarcommon.messaging.request.EntityData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EntityIdData extends EntityData {
    private String id;
}
