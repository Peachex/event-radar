package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public abstract class AbstractEventDTO extends AbstractDTO {
    private String id;
    private String title;
    private LocationDTO location;
    private String dateStr;
    private EventDate date;
    private String category;
    private EventSourceType sourceType;

    public AbstractEventDTO(String title, LocationDTO location, String dateStr, EventDate date, String category,
                            EventSourceType sourceType) {
        this.title = title;
        this.location = location;
        this.dateStr = dateStr;
        this.date = date;
        this.category = category;
        this.sourceType = sourceType;
    }
}
