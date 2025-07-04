package by.klevitov.eventradarcommon.util;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public final class IdGenerator {
    private static final int LENGTH = 24;

    private IdGenerator() {
    }

    public static String generateId() {
        return randomAlphanumeric(LENGTH);
    }
}
