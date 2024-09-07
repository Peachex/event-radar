package by.klevitov.eventpersistor.messaging.comnon.request.dto.data;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.persistor.entity.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MultipleEventData extends EntityData {
    private List<AbstractEvent> events;
}
