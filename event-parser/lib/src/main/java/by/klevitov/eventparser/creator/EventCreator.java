package by.klevitov.eventparser.creator;

import by.klevitov.eventparser.dto.AbstractEventDTO;

import java.util.Map;

public interface EventCreator {
    AbstractEventDTO create(final Map<String, String> fields);
}
