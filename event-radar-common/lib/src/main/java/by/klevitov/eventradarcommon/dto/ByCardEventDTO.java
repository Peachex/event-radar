package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class ByCardEventDTO extends AbstractEventDTO {
    private String priceStr;
    private EventPrice price;
    private String eventLink;
    private String imageLink;
}
