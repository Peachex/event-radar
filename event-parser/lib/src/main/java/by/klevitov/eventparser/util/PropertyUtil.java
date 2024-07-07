package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.ReadingPropertiesFromFileException;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_READING_PROPERTIES_FROM_FILE;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_PROPERTIES_FILE_NAME;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_PROPERTY_KEY;
import static by.klevitov.eventparser.constant.ExceptionMessage.PROPERTIES_FILE_NOT_FOUND;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public final class PropertyUtil {

    private PropertyUtil() {
    }

    public static String retrieveProperty(final String key, final String fileName) {
        validatePropertyKeyAndFileName(key, fileName);
        Properties properties = readProperties(fileName);
        return properties.getProperty(key);
    }

    private static void validatePropertyKeyAndFileName(final String key, final String fileName) {
        throwExceptionInCaseOfEmptyFileName(fileName);
        throwExceptionInCaseOfEmptyPropertyKey(key);
    }

    private static void throwExceptionInCaseOfEmptyFileName(final String fileName) {
        if (isEmpty(fileName)) {
            String exceptionMessage = String.format(NULL_OR_EMPTY_PROPERTIES_FILE_NAME, fileName);
            log.error(exceptionMessage);
            throw new ReadingPropertiesFromFileException(exceptionMessage);
        }
    }

    private static void throwExceptionInCaseOfEmptyPropertyKey(final String propertyKey) {
        if (isEmpty(propertyKey)) {
            String exceptionMessage = String.format(NULL_OR_EMPTY_PROPERTY_KEY, propertyKey);
            log.error(exceptionMessage);
            throw new ReadingPropertiesFromFileException(exceptionMessage);
        }
    }

    private static Properties readProperties(final String fileName) {
        final Properties properties = new Properties();
        try (InputStream input = PropertyUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            throwExceptionInCaseOfFileNotFound(input, fileName);
            properties.load(input);
        } catch (IOException e) {
            throwReadingPropertiesFromFileException(e);
        }
        return properties;
    }

    private static void throwExceptionInCaseOfFileNotFound(final InputStream input, final String fileName) {
        if (input == null) {
            final String exceptionMessage = String.format(PROPERTIES_FILE_NOT_FOUND, fileName);
            log.error(exceptionMessage);
            throw new ReadingPropertiesFromFileException(exceptionMessage);
        }
    }

    private static void throwReadingPropertiesFromFileException(final IOException e) {
        final String exceptionMessage = String.format(ERROR_READING_PROPERTIES_FROM_FILE, e.getMessage());
        log.error(exceptionMessage);
        throw new ReadingPropertiesFromFileException(exceptionMessage);
    }
}
