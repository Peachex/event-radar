package by.klevitov.eventparser.parser;

import by.klevitov.eventradarcommon.dto.AbstractEventDTO;
import org.jsoup.nodes.Document;

import java.util.List;

public interface EventParser {
    List<AbstractEventDTO> parse(final Document htmlDocument);

    String retrieveSiteURL();
}
