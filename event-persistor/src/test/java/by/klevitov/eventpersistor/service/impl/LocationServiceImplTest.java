package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.common.dto.PageRequestDTO;
import by.klevitov.eventpersistor.common.util.PageRequestValidator;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.exception.LocationAlreadyExistsException;
import by.klevitov.eventpersistor.exception.LocationInUseException;
import by.klevitov.eventpersistor.exception.LocationNotFoundException;
import by.klevitov.eventpersistor.exception.LocationValidatorException;
import by.klevitov.eventpersistor.repository.EventMongoRepository;
import by.klevitov.eventpersistor.repository.LocationMongoRepository;
import by.klevitov.eventpersistor.service.LocationService;
import by.klevitov.eventpersistor.util.LocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationServiceImplTest {
    private LocationService service;
    private LocationMongoRepository locationRepository;
    private EventMongoRepository eventRepository;

    @BeforeEach
    public void setUp() {
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

            verify(locationRepository, never()).insert(any(Location.class));
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
            assertThrows(LocationValidatorException.class, () ->
                    service.create(location));

            verify(locationRepository, never()).insert(any(Location.class));
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
            when(locationRepository.findByCountryAndCityIgnoreCase(anyList()))
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
            assertThrows(LocationValidatorException.class, () ->
                    service.create(locations));

            verify(locationRepository, never()).saveAll(anyList());
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
        assertThrows(LocationNotFoundException.class, () -> service.findById("id"));
    }

    @Test
    public void test_findById_withInvalidId() {
        assertThrows(LocationValidatorException.class, () -> service.findById(null));
    }

    @Test
    public void test_findByFields_withExistentFields_withoutPagination() {
        when(locationRepository.findByFields(anyMap(), anyBoolean()))
                .thenReturn(List.of(
                        new Location("id1", "country1", "city1"),
                        new Location("id2", "country1", "city2")
                ));
        List<Location> expected = List.of(
                new Location("id1", "country1", "city1"),
                new Location("id2", "country1", "city2")
        );
        List<Location> actual = service.findByFields(Map.of("country", "country1"), false);
        assertEquals(expected, actual);
    }

    @Test
    public void test_findByFields_withExistentFieldsAnd_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findByFields(anyMap(), anyBoolean(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(
                            new Location("id1", "country1", "city1"),
                            new Location("id2", "country1", "city2")
                    ), Pageable.ofSize(3), 3));
            Page<Location> expected = new PageImpl<>(List.of(
                    new Location("id1", "country1", "city1"),
                    new Location("id2", "country1", "city2")
            ), Pageable.ofSize(3), 3);
            Page<Location> actual = service.findByFields(Map.of("country", "country1"), false,
                    new PageRequestDTO(1, 3, null));
            assertEquals(expected, actual);
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
    }

    @Test
    public void test_findByFields_withNonExistentFields_withoutPagination() {
        when(locationRepository.findByFields(anyMap(), anyBoolean()))
                .thenReturn(new ArrayList<>());
        List<Location> actual = service.findByFields(Map.of("nonExistentField", "fieldValue"), false);
        assertEquals(0, actual.size());
    }

    @Test
    public void test_findByFields_withNonExistentFields_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findByFields(anyMap(), anyBoolean(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(new ArrayList<>()));
            Page<Location> actual = service.findByFields(Map.of("nonExistentField", "fieldValue"), false,
                    new PageRequestDTO(1, 3, null));
            assertEquals(0, actual.getContent().size());
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
    }

    @Test
    public void test_findAll_withoutPagination() {
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
    public void test_findAll_withPagination() {
        try (MockedStatic<PageRequestValidator> validator = Mockito.mockStatic(PageRequestValidator.class)) {
            validator.when(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findAll(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(
                            new Location("id1", "country1", "city1"),
                            new Location("id2", "country2", "city2"),
                            new Location("id3", "country3", "city3")
                    ), Pageable.ofSize(3), 3));
            Page<Location> expected = new PageImpl<>(List.of(
                    new Location("id1", "country1", "city1"),
                    new Location("id2", "country2", "city2"),
                    new Location("id3", "country3", "city3")
            ), Pageable.ofSize(3), 3);
            Page<Location> actual = service.findAll(new PageRequestDTO(1, 3, null));
            assertEquals(expected, actual);
            verifyLocationsId(expected.getContent(), actual.getContent());
            validator.verify(() -> PageRequestValidator.validatePageRequest(any(PageRequestDTO.class)));
        }
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

    @Test
    public void test_update_withValidNonExistentLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeUpdating(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findById(anyString()))
                    .thenReturn(Optional.empty());

            Location updatedLocation = new Location("nonExistentLocationId", "newCountry", null);
            assertThrows(LocationNotFoundException.class, () -> service.update(updatedLocation));

            verify(locationRepository, times(1)).findById(anyString());
            verify(locationRepository, never()).save(any());
            verify(locationRepository, never()).findByCountryAndCityIgnoreCase(anyString(),
                    anyString());
        }
    }

    @Test
    public void test_update_withInvalidLocation() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeUpdating(any(Location.class)))
                    .thenThrow(new LocationValidatorException("Location id cannot be null or empty."));

            Location updatedLocation = new Location(null, null, null);
            assertThrows(LocationValidatorException.class, () -> service.update(updatedLocation));

            verify(locationRepository, never()).findById(anyString());
            verify(locationRepository, never()).save(any());
            verify(locationRepository, never()).findByCountryAndCityIgnoreCase(anyString(),
                    anyString());
        }
    }

    @Test
    public void test_update_withValidLocationThatAlreadyExistsAfterUpdating() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.validateLocationBeforeUpdating(any(Location.class)))
                    .then(invocationOnMock -> null);
            when(locationRepository.findById(anyString()))
                    .thenReturn(Optional.of(new Location("id", "country", "city")));
            when(locationRepository.findByCountryAndCityIgnoreCase(anyString(), anyString()))
                    .thenReturn(Optional.of(new Location("other_id", "updatedCountry", "city")));

            Location updatedLocation = new Location("id", "updatedCountry", "city");
            assertThrows(LocationAlreadyExistsException.class,
                    () -> service.update(updatedLocation));

            verify(locationRepository, times(1)).findById(anyString());
            verify(locationRepository, never()).save(any());
            verify(locationRepository, times(1)).findByCountryAndCityIgnoreCase(anyString(),
                    anyString());
        }
    }

    @Test
    public void test_delete_withValidExistentLocationIdThatIsNotInUse() {
        when(locationRepository.findById(anyString()))
                .thenReturn(Optional.of(new Location("id", "country", "city")));
        when(eventRepository.countByLocation(any()))
                .thenReturn(0L);

        service.delete("id");

        verify(locationRepository, times(1)).findById(anyString());
        verify(eventRepository, times(1)).countByLocation(any());
        verify(locationRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void test_delete_withValidExistentLocationIdThatIsInUse() {
        when(locationRepository.findById(anyString()))
                .thenReturn(Optional.of(new Location("id", "country", "city")));
        when(eventRepository.countByLocation(any()))
                .thenReturn(1L);

        assertThrows(LocationInUseException.class, () -> service.delete("id"));

        verify(locationRepository, times(1)).findById(anyString());
        verify(eventRepository, times(1)).countByLocation(any());
        verify(locationRepository, never()).deleteById(anyString());
    }

    @Test
    public void test_delete_withInvalidLocationId() {
        try (MockedStatic<LocationValidator> validator = Mockito.mockStatic(LocationValidator.class)) {
            validator.when(() -> LocationValidator.throwExceptionInCaseOfEmptyId(anyString()))
                    .thenThrow(new LocationValidatorException("Location id cannot be null or empty."));

            assertThrows(LocationValidatorException.class, () -> service.delete("id"));

            verify(locationRepository, never()).findById(anyString());
            verify(eventRepository, never()).countByLocation(any());
            verify(locationRepository, never()).deleteById(anyString());
        }
    }
}
