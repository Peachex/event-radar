package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractEventDTO {
    private String title;
    private Location location;
    private EventDate date;
    private String category;
    private EventSourceType sourceType;
}
