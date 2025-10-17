package by.klevitov.eventparser.configuration;

import by.klevitov.eventparser.creator.impl.AfishaRelaxEventCreator;
import by.klevitov.eventparser.creator.impl.ByCardEventCreator;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.parser.impl.AfishaRelaxEventParser;
import by.klevitov.eventparser.parser.impl.ByCardEventParser;
import by.klevitov.eventradarcommon.dto.EventSourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public final class EventParserConfiguration {
    @Getter
    @Setter
    private static long requestsDelayMillis = 3000L;

    @Getter
    private static final Map<EventSourceType, EventParser> availableParsers = Map.of(
            EventSourceType.AFISHA_RELAX, new AfishaRelaxEventParser(new AfishaRelaxEventCreator()),
            EventSourceType.BYCARD, new ByCardEventParser(new ByCardEventCreator())
    );

    private EventParserConfiguration() {
    }

    public static boolean parserIsUnknown(final EventParser parser) {
        return !availableParsers.containsValue(parser);
    }
}
