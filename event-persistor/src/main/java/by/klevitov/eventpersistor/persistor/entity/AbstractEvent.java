package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventradarcommon.dto.EventDate;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Document(collection = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractEvent {
    @Id
    private String id;
    private String title;
    @DBRef
    private Location location;
    private String dateStr;
    private EventDate date;
    private String category;
    private EventSourceType sourceType;

    public String createKeyForComparing() {
        return String.format("%s:%s:%s", title.toLowerCase(), category, sourceType.name().toLowerCase());
    }

    public void copyValuesForNullOrEmptyFieldsFromEvent(final AbstractEvent source) {
        id = (isEmpty(id) ? source.id : id);
        title = (isEmpty(title) ? source.title : title);
        dateStr = (isEmpty(dateStr) ? source.dateStr : dateStr);
        date = (date == null ? source.date : date);
        category = (isEmpty(category) ? source.category : category);
        sourceType = source.sourceType;
        location = createUpdatedLocation(source.location);
    }

    private Location createUpdatedLocation(final Location source) {
        if (location == null) {
            return source;
        }
        location.setCountry(isEmpty(location.getCountry()) ? source.getCountry() : location.getCountry());
        location.setCity(isEmpty(location.getCity()) ? source.getCity() : location.getCity());
        return location;
    }
}
