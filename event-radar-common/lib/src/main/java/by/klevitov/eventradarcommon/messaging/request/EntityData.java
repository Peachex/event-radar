package by.klevitov.eventradarcommon.messaging.request;

import by.klevitov.eventradarcommon.messaging.request.data.EntityIdData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleEventData;
import by.klevitov.eventradarcommon.messaging.request.data.MultipleLocationData;
import by.klevitov.eventradarcommon.messaging.request.data.SearchFieldsData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleEventData;
import by.klevitov.eventradarcommon.messaging.request.data.SingleLocationData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityIdData.class, name = "entityIdData"),
        @JsonSubTypes.Type(value = MultipleEventData.class, name = "multipleEventData"),
        @JsonSubTypes.Type(value = MultipleLocationData.class, name = "multipleLocationData"),
        @JsonSubTypes.Type(value = SearchFieldsData.class, name = "searchFieldsData"),
        @JsonSubTypes.Type(value = SingleEventData.class, name = "singleEventData"),
        @JsonSubTypes.Type(value = SingleLocationData.class, name = "singleLocationData")
})
public abstract class EntityData {
}
