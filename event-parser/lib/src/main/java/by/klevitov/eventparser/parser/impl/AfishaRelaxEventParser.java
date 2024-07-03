package by.klevitov.eventparser.parser.impl;

import by.klevitov.eventparser.dto.AbstractEventDTO;
import by.klevitov.eventparser.parser.EventParser;
import by.klevitov.eventparser.util.PropertyUtil;
import lombok.extern.java.Log;
import org.jsoup.nodes.Document;

import java.util.List;

import static by.klevitov.eventparser.constant.PropertyConstant.AFISHA_RELAX_SITE_URL;
import static by.klevitov.eventparser.constant.PropertyConstant.PROPERTY_FILE_WITH_SITES_FOR_PARSING;

@Log
public class AfishaRelaxEventParser implements EventParser {
    private static final String SITE_URL = PropertyUtil.retrieveProperty(AFISHA_RELAX_SITE_URL,
            PROPERTY_FILE_WITH_SITES_FOR_PARSING);

    @Override
    public List<AbstractEventDTO> parse(Document htmlDocument) {
        return List.of();
    }

    @Override
    public String retrieveSiteURL() {
        return SITE_URL;
    }
}
