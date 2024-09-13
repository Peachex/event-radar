package by.klevitov.eventradarcommon.messaging.request.data;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import by.klevitov.eventradarcommon.messaging.request.EntityData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SingleEventData extends EntityData {
    private AbstractEventDTO eventDTO;
}
