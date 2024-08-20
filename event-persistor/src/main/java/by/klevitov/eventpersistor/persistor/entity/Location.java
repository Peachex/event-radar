package by.klevitov.eventpersistor.persistor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Document(collection = "event_locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Location {
    @Id
    private String id;
    private String country;
    private String city;

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String createIdBasedOnCountryAndCity() {
        return String.format("%s:%s", country.toLowerCase(), city.toLowerCase());
    }

    public void copyValuesForNullOrEmptyFieldsFromLocation(final Location source) {
        id = (isEmpty(id) ? source.id : id);
        country = (isEmpty(country) ? source.country : country);
        city = (isEmpty(city) ? source.city : city);
    }
}
