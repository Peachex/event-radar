package by.klevitov.eventpersistor.messaging.comnon.request.dto.data;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SingleEventData extends EntityData {
    private AbstractEventDTO eventDTO;
}
