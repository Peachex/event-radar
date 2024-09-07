package by.klevitov.eventpersistor.messaging.comnon.request.dto.data;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import by.klevitov.eventpersistor.persistor.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MultipleLocationData extends EntityData {
    private List<Location> locations;
}
