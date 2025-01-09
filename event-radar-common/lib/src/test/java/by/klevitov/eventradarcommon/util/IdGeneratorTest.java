package by.klevitov.eventradarcommon.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdGeneratorTest {
    @Test
    public void test_generateId() throws Exception {
        Field length = IdGenerator.class.getDeclaredField("LENGTH");
        length.setAccessible(true);
        int expectedLength = length.getInt(IdGenerator.class);
        int actualLength = IdGenerator.generateId().length();
        assertEquals(expectedLength, actualLength);
    }
}
