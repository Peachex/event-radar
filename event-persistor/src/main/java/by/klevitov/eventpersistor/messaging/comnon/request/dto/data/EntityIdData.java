package by.klevitov.eventpersistor.messaging.comnon.request.dto.data;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EntityIdData extends EntityData {
    private String id;
}
