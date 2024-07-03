package by.klevitov.eventparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractEventDTO {
    //todo Add fields, implementations and move all into the separate common library.
    private String title;
    private Location location;
    private String category;
    private EventSourceType sourceType;
}
