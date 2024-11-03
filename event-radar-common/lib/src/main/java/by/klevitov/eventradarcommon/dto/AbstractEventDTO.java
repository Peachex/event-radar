package by.klevitov.eventradarcommon.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "sourceType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AfishaRelaxEventDTO.class, name = "AFISHA_RELAX"),
        @JsonSubTypes.Type(value = ByCardEventDTO.class, name = "BYCARD")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "id")
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
