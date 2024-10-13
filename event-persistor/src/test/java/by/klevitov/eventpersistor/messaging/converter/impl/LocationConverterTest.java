package by.klevitov.eventpersistor.messaging.converter.impl;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.exception.EntityConverterException;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class LocationConverterTest {
    private static EntityConverter converter;

    @BeforeAll
    public static void init() {
        converter = new LocationConverter();
    }

    @Test
    public void test_convertFromDTO_withNotNullDTO() {
        AbstractDTO locationDTO = new LocationDTO("country", "city");
        AbstractEntity expected = new Location("country", "city");
        AbstractEntity actual = converter.convertFromDTO(locationDTO);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertFromDTO_withNullDTO() {
        AbstractDTO nullDTO = null;
        assertThrowsExactly(EntityConverterException.class, () -> converter.convertFromDTO(nullDTO));
    }

    @Test
    public void test_convertToDTO_withNotNullDTO() {
        AbstractEntity location = new Location("country", "city");
        AbstractDTO expected = new LocationDTO("country", "city");
        AbstractDTO actual = converter.convertToDTO(location);
        assertEquals(expected, actual);
    }

    @Test
    public void test_convertToDTO_withNullDTO() {
        AbstractEntity entity = null;
        assertThrowsExactly(EntityConverterException.class, () -> converter.convertToDTO(entity));
    }
}
