package by.klevitov.eventradarcommon.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbstractEventDTO.class, name = "abstractEventDTO"),
        @JsonSubTypes.Type(value = AfishaRelaxEventDTO.class, name = "afishaRelaxEventDTO"),
        @JsonSubTypes.Type(value = ByCardEventDTO.class, name = "byCardEventDTO"),
        @JsonSubTypes.Type(value = LocationDTO.class, name = "locationDTO")
})
public abstract class AbstractDTO {
}
