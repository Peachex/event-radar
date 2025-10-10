package by.klevitov.eventpersistor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Document(collection = "event_locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "id")
public class Location extends AbstractEntity {
    @Id
    private String id;
    private String name;
    private String country;
    private String city;
    private String rawAddress;
    private double latitude;
    private double longitude;

    public Location(String id, String country, String city) {
        this(country, city);
        this.id = id;
    }

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String createIdBasedOnRawAddressAndName() {
        return String.format("%s:%s", rawAddress.toLowerCase(), name.toLowerCase());
    }

    public void copyValuesForNullOrEmptyFieldsFromLocation(final Location source) {
        id = (isEmpty(id) ? source.id : id);
        name = (isEmpty(name) ? source.name : name);
        country = (isEmpty(country) ? source.country : country);
        city = (isEmpty(city) ? source.city : city);
        rawAddress = (isEmpty(rawAddress) ? source.rawAddress : rawAddress);
        latitude = (latitude == 0 ? source.latitude : latitude);
        longitude = (longitude == 0 ? source.longitude : longitude);
    }

    public void updateRawAddressAndNameWithDefaultsIfNull() {
        if (rawAddress == null) {
            rawAddress = EMPTY;
        }
        if (name == null) {
            name = EMPTY
            ;
        }
    }
}
