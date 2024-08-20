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
        this.id = (this.id == null ? source.id : id);
        this.title = (this.title == null ? source.title : title);
        this.dateStr = (this.dateStr == null ? source.dateStr : dateStr);
        this.date = (this.date == null ? source.date : date);
        this.category = (this.category == null ? source.category : category);
        this.sourceType = source.sourceType;
        this.location = createUpdatedLocation(source.location);
    }

    private Location createUpdatedLocation(final Location source) {
        if (this.location == null) {
            return source;
        }
        this.location.setCountry(this.location.getCountry() == null
                ? source.getCountry() : location.getCountry());
        this.location.setCity(this.location.getCity() == null
                ? source.getCity() : location.getCity());
        return location;
    }
}
