package by.klevitov.eventpersistor.persistor.service;

import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.exception.LocationServiceException;
import by.klevitov.eventpersistor.persistor.exception.LocationValidatorException;
import by.klevitov.eventpersistor.persistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.persistor.repository.LocationMongoRepository;
import by.klevitov.eventpersistor.persistor.service.impl.LocationServiceImpl;
import by.klevitov.eventpersistor.persistor.util.LocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationServiceImplTest {
    private LocationService service;
    private LocationMongoRepository locationRepository;
    private EventMongoRepository eventRepository;

    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationMongoRepository.class);
        eventRepository = Mockito.mock(EventMongoRepository.class);
        service = new LocationServiceImpl(locationRepository, eventRepository);
    }

    @Test
    public void test_create_withValidUniqueSingleLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findByCountryAndCityIgnoreCase(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(Optional.empty());

            Location location = new Location("country", "city");
            when(locationRepository.insert(any(Location.class)))
                    .thenReturn(new Location("id", location.getCountry(), location.getCity()));

            Location expected = new Location("id", location.getCountry(), location.getCity());
            Location actual = service.create(location);

            verify(locationRepository, times(1)).insert(any(Location.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withValidNonUniqueSingleLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .then(invocationOnMock -> null);

            Location location = new Location("country", "city");
            when(locationRepository.findByCountryAndCityIgnoreCase(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(Optional.of(new Location("id", location.getCountry(), location.getCity())));

            Location expected = new Location("id", location.getCountry(), location.getCity());
            Location actual = service.create(location);

            verify(locationRepository, times(0)).insert(any(Location.class));
            assertEquals(expected, actual);
            assertEquals(expected.getId(), actual.getId());
        }
    }

    @Test
    public void test_create_withInvalidSingleLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .thenThrow(new LocationValidatorException("Location country cannot be null or empty."));

            Location location = new Location(null, null);
            Exception exception = assertThrows(LocationValidatorException.class, () ->
                    service.create(location));

            String expectedMessage = "Location country cannot be null or empty.";
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_create_withValidUniqueMultipleLocations() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findAll())
                    .thenReturn(new ArrayList<>());

            List<Location> locations = List.of(
                    new Location("country1", "city1"),
                    new Location("country2", "city2"),
                    new Location("country3", "city3")
            );
            when(locationRepository.saveAll(anyList()))
                    .thenReturn(List.of(
                            new Location("id1", locations.get(0).getCountry(), locations.get(0).getCity()),
                            new Location("id2", locations.get(1).getCountry(), locations.get(1).getCity()),
                            new Location("id3", locations.get(2).getCountry(), locations.get(2).getCity())));

            List<Location> expected = List.of(
                    new Location("id1", locations.get(0).getCountry(), locations.get(0).getCity()),
                    new Location("id2", locations.get(1).getCountry(), locations.get(1).getCity()),
                    new Location("id3", locations.get(2).getCountry(), locations.get(2).getCity())
            );
            List<Location> actual = service.create(locations);

            verify(locationRepository, times(1)).saveAll(anyList());
            assertEquals(expected, actual);
            verifyLocationsId(expected, actual);
        }
    }

    @Test
    public void test_create_withValidNonUniqueMultipleLocations() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findAll())
                    .thenAnswer(invocationOnMock -> {
                        List<Location> existentLocations = new ArrayList<>();
                        existentLocations.add(new Location("id1", "country1", "city1"));
                        existentLocations.add(new Location("id2", "country2", "city2"));
                        return existentLocations;
                    });

            List<Location> locations = List.of(
                    new Location("country1", "city1"),
                    new Location("country2", "city2"),
                    new Location("country3", "city3")
            );
            when(locationRepository.saveAll(anyList()))
                    .thenReturn(List.of(
                            new Location("id3", locations.get(2).getCountry(), locations.get(2).getCity())
                    ));

            List<Location> expected = List.of(
                    new Location("id1", locations.get(0).getCountry(), locations.get(0).getCity()),
                    new Location("id2", locations.get(1).getCountry(), locations.get(1).getCity()),
                    new Location("id3", locations.get(2).getCountry(), locations.get(2).getCity())
            );
            List<Location> actual = service.create(locations);

            verify(locationRepository, times(1)).saveAll(anyList());
            assertEquals(expected, actual);
            verifyLocationsId(expected, actual);
        }
    }

    private void verifyLocationsId(final List<Location> expected, final List<Location> actual) {
        for (int i = 0; i < expected.size() && i < actual.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
        }
    }

    @Test
    public void test_create_withInvalidMultipleLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeCreation(any(Location.class)))
                    .thenThrow(new LocationValidatorException("Location country cannot be null or empty."));

            List<Location> locations = List.of(
                    new Location(null, null),
                    new Location(null, null),
                    new Location(null, null)
            );
            Exception exception = assertThrows(LocationValidatorException.class, () ->
                    service.create(locations));

            String expectedMessage = "Location country cannot be null or empty.";
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_findById_withValidIdAndExistentLocation() {
        when(locationRepository.findById(anyString()))
                .thenReturn(Optional.of(new Location("id", "country", "city")));
        Location expected = new Location("id", "country", "city");
        Location actual = service.findById("id");
        assertEquals(expected, actual);
    }

    @Test
    public void test_findById_withValidIdAndNonExistentLocation() {
        when(locationRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(LocationServiceException.class, () -> service.findById("id"));
        String expectedMessage = "Cannot find location with id: 'id'";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_findById_withInvalidId() {
        Exception exception = assertThrows(LocationValidatorException.class, () -> service.findById(null));
        String expectedMessage = "Location id cannot be null or empty.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_findByFields_withExistentFields() {
        when(locationRepository.findByFields(anyMap()))
                .thenReturn(List.of(
                        new Location("id1", "country1", "city1"),
                        new Location("id2", "country1", "city2")
                ));
        List<Location> expected = List.of(
                new Location("id1", "country1", "city1"),
                new Location("id2", "country1", "city2")
        );
        List<Location> actual = service.findByFields(Map.of("country", "country1"));
        assertEquals(expected, actual);
    }

    @Test
    public void test_findByFields_withNonExistentFields() {
        when(locationRepository.findByFields(anyMap()))
                .thenReturn(new ArrayList<>());
        List<Location> actual = service.findByFields(Map.of("nonExistentField", "fieldValue"));
        assertEquals(0, actual.size());
    }

    @Test
    public void test_findAll() {
        when(locationRepository.findAll())
                .thenReturn(List.of(
                        new Location("id1", "country1", "city1"),
                        new Location("id2", "country2", "city2"),
                        new Location("id3", "country3", "city3")
                ));
        List<Location> expected = List.of(
                new Location("id1", "country1", "city1"),
                new Location("id2", "country2", "city2"),
                new Location("id3", "country3", "city3")
        );
        List<Location> actual = service.findAll();
        assertEquals(expected, actual);
        verifyLocationsId(expected, actual);
    }

    @Test
    public void test_update_withValidExistentLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeUpdating(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findById(anyString()))
                    .thenReturn(Optional.of(new Location("id", "oldCountry", "city")));
            when(locationRepository.findByCountryAndCityIgnoreCase(anyString(), anyString()))
                    .thenReturn(Optional.empty());

            Location updatedLocation = new Location("id", "newCountry", null);
            when(locationRepository.save(updatedLocation))
                    .thenReturn(new Location("id", updatedLocation.getCountry(), "city"));

            Location expected = new Location("id", "newCountry", "city");
            Location actual = service.update(updatedLocation);
            assertEquals(expected, actual);
        }
    }
}
