package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractEventDTO extends AbstractDTO {
    private String title;
    private LocationDTO location;
    private String dateStr;
    private EventDate date;
    private String category;
    private EventSourceType sourceType;
}
