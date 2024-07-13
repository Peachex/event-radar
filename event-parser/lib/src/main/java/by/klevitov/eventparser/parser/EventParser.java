package by.klevitov.eventparser.parser;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static by.klevitov.eventparser.constant.EventField.END_DATE;
import static by.klevitov.eventparser.constant.EventField.START_DATE;

public interface EventParser {
    List<AbstractEventDTO> parse(final Document htmlDocument);

    String retrieveSiteURL();

    static void addDatesToMap(final Map<String, String> fields, final Pair<LocalDate, LocalDate> dates) {
        fields.put(START_DATE, dates.getLeft() != null ? dates.getLeft().toString() : null);
        fields.put(END_DATE, dates.getRight() != null ? dates.getRight().toString() : null);
    }
}
