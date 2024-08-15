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
public class AbstractEvent {
    @Id
    private String id;
    private String title;
    @DBRef
    private Location location;
    private String dateStr;
    private EventDate date;
    private String category;
    private EventSourceType sourceType;

    public String createIdBasedOnTitleAndSourceType() {
        return String.format("%s:%s", title.toLowerCase(), sourceType.name().toLowerCase());
    }
}
