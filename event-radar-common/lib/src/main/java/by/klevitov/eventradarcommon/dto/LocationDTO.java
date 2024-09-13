package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocationDTO extends AbstractDTO {
    private String id;
    private String country;
    private String city;

    public LocationDTO(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
