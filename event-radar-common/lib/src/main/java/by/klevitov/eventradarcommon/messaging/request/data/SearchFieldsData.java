package by.klevitov.eventradarcommon.messaging.request.data;

import by.klevitov.eventradarcommon.messaging.request.EntityData;
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
