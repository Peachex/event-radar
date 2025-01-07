package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.ReadingPropertiesFromFileException;
import org.junit.jupiter.api.Test;

import static by.klevitov.eventparser.util.PropertyUtil.retrieveProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PropertyUtilTest {
    private static final String PROPERTY_FILE_NAME = "properties.yml";
    private static final String PROPERTY_NAME = "test-property";

    @Test
    public void test_retrieveProperty_withValidFileAndKey() {
        String expected = "testValue";
        String actual = retrieveProperty(PROPERTY_NAME, PROPERTY_FILE_NAME);
        assertEquals(expected, actual);
    }

    @Test
    public void test_retrieveProperty_withValidFileAndNotExistingKey() {
        String key = "not_existing_key";
        String actual = retrieveProperty(key, PROPERTY_FILE_NAME);
        assertNull(actual);
    }

    @Test
    public void test_retrieveProperty_withNullFileName() {
        String fileName = null;
        assertThrows(ReadingPropertiesFromFileException.class, () -> retrieveProperty(PROPERTY_NAME, fileName));
    }

    @Test
    public void test_retrieveProperty_withEmptyFileName() {
        String fileName = "";
        assertThrows(ReadingPropertiesFromFileException.class, () -> retrieveProperty(PROPERTY_NAME, fileName));
    }

    @Test
    public void test_retrieveProperty_withFileNameAndNullKey() {
        String key = null;
        assertThrows(ReadingPropertiesFromFileException.class, () -> retrieveProperty(key, PROPERTY_FILE_NAME));
    }

    @Test
    public void test_retrieveProperty_withFileNameAndEmptyKey() {
        String key = "";
        assertThrows(ReadingPropertiesFromFileException.class, () -> retrieveProperty(key, PROPERTY_FILE_NAME));
    }

    @Test
    public void test_retrieveProperty_withInvalidFileNameAndValidKey() {
        assertThrows(ReadingPropertiesFromFileException.class,
                () -> retrieveProperty(PROPERTY_NAME, "invalid_file.yml"));
    }
}
