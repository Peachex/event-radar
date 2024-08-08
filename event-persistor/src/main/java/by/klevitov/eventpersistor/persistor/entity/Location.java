package by.klevitov.eventpersistor.persistor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    //todo Instances of this class should be saved in MongoDB separately of Event instance.
}
