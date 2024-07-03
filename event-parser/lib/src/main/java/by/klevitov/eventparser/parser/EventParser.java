package by.klevitov.eventparser.parser;

import by.klevitov.eventparser.dto.AbstractEventDTO;
import org.jsoup.nodes.Document;

import java.util.List;

public interface EventParser {
    List<AbstractEventDTO> parse(Document htmlDocument);

    String retrieveSiteURL();
}
