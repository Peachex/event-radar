package by.klevitov.eventpersistor.messaging.converter.impl;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.EntityConverterException;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.AfishaRelaxEvent;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.AfishaRelaxEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AfishaRelaxEventConverterTest {
    private static EntityConverter eventConverter;
    private static LocationConverter locationConverter;

    @BeforeAll
    public static void init() {
        locationConverter = Mockito.mock(LocationConverter.class);
        eventConverter = new AfishaRelaxEventConverter(locationConverter);
    }

    @Test
    public void test_convertFromDTO_withNotNullDTO() {
        when(locationConverter.convertFromDTO(any(LocationDTO.class)))
                .thenReturn(new Location("country", "city"));

        AbstractDTO eventDTO = AfishaRelaxEventDTO.builder()
                .title("title")
                .location(new LocationDTO("country", "city"))
                .build();

        AbstractEntity expected = AfishaRelaxEvent.builder()
                .title("title")
                .location(new Location("country", "city"))
                .build();
        AbstractEntity actual = eventConverter.convertFromDTO(eventDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertFromDTO_withNullDTO() {
        AbstractDTO nullDTO = null;
        assertThrowsExactly(EntityConverterException.class, () -> eventConverter.convertFromDTO(nullDTO));
    }

    @Test
    public void test_convertToDTO_withNotNullDTO() {
        when(locationConverter.convertToDTO(any(Location.class)))
                .thenReturn(new LocationDTO("country", "city"));

        AbstractEntity event = AfishaRelaxEvent.builder()
                .title("title")
                .location(new Location("country", "city"))
                .build();

        AbstractDTO expected = AfishaRelaxEventDTO.builder()
                .title("title")
                .location(new LocationDTO("country", "city"))
                .build();
        AbstractDTO actual = eventConverter.convertToDTO(event);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertToDTO_withNullDTO() {
        AbstractEntity entity = null;
        assertThrowsExactly(EntityConverterException.class, () -> eventConverter.convertToDTO(entity));
    }
}
