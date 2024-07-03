package by.klevitov.eventparser.configuration;

import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.parser.impl.AfishaRelaxEventParser;
import lombok.Getter;

import java.util.List;

public final class EventParserConfiguration {
    @Getter
    private static final List<EventParser> availableParsers = List.of(new AfishaRelaxEventParser());

    //todo add parsers.

    private EventParserConfiguration() {
    }

    public static boolean parserIsUnknown(EventParser parser) {
        return !availableParsers.contains(parser);
    }
}
