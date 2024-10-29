package by.klevitov.eventradarcommon.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(AbstractEventDTO.class),
        @JsonSubTypes.Type(value = AfishaRelaxEventDTO.class),
        @JsonSubTypes.Type(value = ByCardEventDTO.class),
        @JsonSubTypes.Type(value = LocationDTO.class)
})
public abstract class AbstractDTO {
}
