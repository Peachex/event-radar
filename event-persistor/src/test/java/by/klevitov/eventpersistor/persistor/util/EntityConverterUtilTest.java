package by.klevitov.eventpersistor.persistor.util;

import by.klevitov.eventpersistor.persistor.exception.EntityConverterException;
import by.klevitov.eventpersistor.persistor.entity.AbstractEntity;
import by.klevitov.eventpersistor.persistor.entity.Location;
import by.klevitov.eventradarcommon.dto.AbstractDTO;
import by.klevitov.eventradarcommon.dto.LocationDTO;
import org.junit.jupiter.api.Test;

import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullDTO;
import static by.klevitov.eventpersistor.persistor.util.EntityConverterUtil.throwExceptionInCaseOfNullEntity;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EntityConverterUtilTest {
    @Test
    public void test_throwExceptionInCaseOfNullDTO_withNotNullDTO() {
        AbstractDTO dto = new LocationDTO();
        assertDoesNotThrow(() -> throwExceptionInCaseOfNullDTO(dto));
    }

    @Test
    public void test_throwExceptionInCaseOfNullDTO_withNullDTO() {
        AbstractDTO nullDTO = null;
        assertThrowsExactly(EntityConverterException.class, () -> throwExceptionInCaseOfNullDTO(nullDTO));
    }

    @Test
    public void test_throwExceptionInCaseOfNullEntity_withNotNullEntity() {
        AbstractEntity entity = new Location();
        assertDoesNotThrow(() -> throwExceptionInCaseOfNullEntity(entity));
    }

    @Test
    public void test_throwExceptionInCaseOfNullEntity_withNullEntity() {
        AbstractEntity nullEntity = null;
        assertThrowsExactly(EntityConverterException.class, () -> throwExceptionInCaseOfNullEntity(nullEntity));
    }
}
