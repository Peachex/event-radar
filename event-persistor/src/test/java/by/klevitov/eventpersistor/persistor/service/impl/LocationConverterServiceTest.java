package by.klevitov.eventpersistor.persistor.service.impl;

import by.klevitov.eventpersistor.persistor.converter.EntityConverter;
import by.klevitov.eventpersistor.persistor.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventpersistor.persistor.factory.EntityConverterFactory;
import by.klevitov.eventpersistor.persistor.service.EntityConverterService;
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
        LocationDTO locationDTO = new LocationDTO("id", "country", "city");
        Location expected = new Location(locationDTO.getId(), locationDTO.getCountry(), locationDTO.getCity());
        when(mockedConverter.convertFromDTO(locationDTO))
                .thenReturn(expected);
        Location actual = service.convertFromDTO(locationDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void tes_convertFromDTO_withMultipleLocationsDTO() {
        List<LocationDTO> locationsDTO = List.of(
                new LocationDTO("id1", "country1", "city1"),
                new LocationDTO("id2", "country2", "city2"),
                new LocationDTO("id3", "country3", "city3")
        );
        List<Location> expected = List.of(
                new Location(locationsDTO.get(0).getId(), locationsDTO.get(0).getCountry(),
                        locationsDTO.get(0).getCity()),
                new Location(locationsDTO.get(1).getId(), locationsDTO.get(1).getCountry(),
                        locationsDTO.get(1).getCity()),
                new Location(locationsDTO.get(2).getId(), locationsDTO.get(2).getCountry(),
                        locationsDTO.get(2).getCity())
        );

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
