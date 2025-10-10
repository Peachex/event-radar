package by.klevitov.eventpersistor.service.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventpersistor.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.service.EntityConverterService;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LocationConverterServiceTest {
    private EntityConverterService<Location, LocationDTO> service;
    private EntityConverter mockedConverter;
    private EntityConverterFactory mockedConverterFactory;

    @BeforeEach
    public void setUp() {
        mockedConverterFactory = Mockito.mock(EntityConverterFactory.class);
        mockedConverter = Mockito.mock(LocationConverter.class);
        service = createServiceWithMockedFactory(mockedConverterFactory, mockedConverter);
    }

    private LocationConverterService createServiceWithMockedFactory(final EntityConverterFactory mockedFactory,
                                                                    final EntityConverter mockedConverter) {
        when(mockedFactory.getConverter(LocationDTO.class))
                .thenReturn(mockedConverter);
        return new LocationConverterService(mockedConverterFactory);
    }

    @Test
    public void tes_convertFromDTO_withSingleLocationDTO() {
        LocationDTO locationDTO = new LocationDTO("id", "name", "country", "city", "rawAddress", 1, 2);
        Location expected = new Location(
                locationDTO.getId(),
                locationDTO.getName(),
                locationDTO.getCountry(),
                locationDTO.getCity(),
                locationDTO.getRawAddress(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude()
        );
        when(mockedConverter.convertFromDTO(locationDTO))
                .thenReturn(expected);
        Location actual = service.convertFromDTO(locationDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void tes_convertFromDTO_withMultipleLocationsDTO() {
        List<LocationDTO> locationsDTO = List.of(
                new LocationDTO("id1", "name1", "country1", "city1", "rawAddress1", 1, 1),
                new LocationDTO("id2", "name2", "country2", "city2", "rawAddress2", 2, 2),
                new LocationDTO("id3", "name3", "country3", "city3", "rawAddress3", 3, 3)
        );
        List<Location> expected = locationsDTO.stream()
                .map(dto -> new Location(
                        dto.getId(),
                        dto.getName(),
                        dto.getCountry(),
                        dto.getCity(),
                        dto.getRawAddress(),
                        dto.getLatitude(),
                        dto.getLongitude()
                ))
                .toList();

        when(mockedConverter.convertFromDTO(locationsDTO.get(0)))
                .thenReturn(expected.get(0));
        when(mockedConverter.convertFromDTO(locationsDTO.get(1)))
                .thenReturn(expected.get(1));
        when(mockedConverter.convertFromDTO(locationsDTO.get(2)))
                .thenReturn(expected.get(2));

        List<Location> actual = service.convertFromDTO(locationsDTO);
        assertEquals(expected, actual);
    }
}
