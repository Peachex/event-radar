package by.klevitov.eventpersistor.messaging.comnon.request.dto.data;

import by.klevitov.eventpersistor.messaging.comnon.request.dto.EntityData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SearchFieldsData extends EntityData {
    private Map<String, Object> fields;
}
