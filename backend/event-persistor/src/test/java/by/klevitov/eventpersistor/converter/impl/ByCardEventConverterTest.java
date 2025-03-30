package by.klevitov.eventpersistor.converter.impl;

import by.klevitov.eventpersistor.converter.EntityConverter;
import by.klevitov.eventpersistor.converter.impl.ByCardEventConverter;
import by.klevitov.eventpersistor.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.exception.EntityConverterException;
import by.klevitov.eventpersistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.entity.ByCardEvent;
import by.klevitov.eventpersistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.ByCardEventDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ByCardEventConverterTest {
    private static EntityConverter eventConverter;
    private static LocationConverter mockedLocationConverter;

    @BeforeAll
    public static void init() {
        mockedLocationConverter = Mockito.mock(LocationConverter.class);
        eventConverter = new ByCardEventConverter(mockedLocationConverter);
    }

    @Test
    public void test_convertFromDTO_withNotNullDTO() {
        when(mockedLocationConverter.convertFromDTO(any(LocationDTO.class)))
                .thenReturn(new Location("country", "city"));

        AbstractDTO eventDTO = ByCardEventDTO.builder()
                .title("title")
                .location(new LocationDTO("country", "city"))
                .build();

        AbstractEntity expected = ByCardEvent.builder()
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
        when(mockedLocationConverter.convertToDTO(any(Location.class)))
                .thenReturn(new LocationDTO("country", "city"));

        AbstractEntity event = ByCardEvent.builder()
                .title("title")
                .location(new Location("country", "city"))
                .build();

        AbstractDTO expected = ByCardEventDTO.builder()
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
