package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "id")
@SuperBuilder
public class LocationDTO extends AbstractDTO {
    private String id;
    private String name;
    private String country;
    private String city;
    private String rawAddress;
    private double latitude;
    private double longitude;

    public LocationDTO(String id, String country, String city) {
        this(country, city);
        this.id = id;
    }

    public LocationDTO(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
