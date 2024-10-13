package by.klevitov.eventpersistor.messaging.factory;

import by.klevitov.eventpersistor.messaging.converter.EntityConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.AfishaRelaxEventConverter;
import by.klevitov.eventpersistor.messaging.converter.impl.LocationConverter;
import by.klevitov.eventpersistor.messaging.exception.EntityConverterFactoryException;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EntityConverterFactoryTest {
    private static EntityConverterFactory factory;
    private static EntityConverter mockedLocationConverter;
    private static EntityConverter mockedAfishaRelaxEventConverter;

    @BeforeAll
    public static void init() {
        mockedLocationConverter = Mockito.mock(LocationConverter.class);
        mockedAfishaRelaxEventConverter = Mockito.mock(AfishaRelaxEventConverter.class);
        factory = new EntityConverterFactory(List.of(mockedLocationConverter, mockedAfishaRelaxEventConverter));
    }

    @Test
    public void test_getConverter_withDTOClassAndExistentConverter() {
        Class<LocationDTO> locationDTOClass = LocationDTO.class;
        EntityConverter expected = mockedLocationConverter;
        EntityConverter actual = factory.getConverter(locationDTOClass);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getConverter_withDTOClassAndNonExistentConverter() {
        Class<AbstractDTO> nullClass = null;
        assertThrowsExactly(EntityConverterFactoryException.class, () -> factory.getConverter(nullClass));
    }

    @Test
    public void test_getConverter_withEventSourceTypeAndExistentConverter() {
        EventSourceType afishaRelaxEventSourceType = EventSourceType.AFISHA_RELAX;
        EntityConverter expected = mockedAfishaRelaxEventConverter;
        EntityConverter actual = factory.getConverter(afishaRelaxEventSourceType);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getConverter_withEventSourceTypeAndNonExistentConverter() {
        EventSourceType nullSourceType = null;
        assertThrowsExactly(EntityConverterFactoryException.class, () -> factory.getConverter(nullSourceType));
    }
}
