package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.exception.InvalidURLException;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_URL;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_DURING_HTML_DOCUMENT_RETRIEVING;

@Log
public final class JsoupUtil {
    private JsoupUtil() {
    }

    public static Document retrieveDocumentByURL(final String url) throws HTMLDocumentRetrievingException {
        try {
            throwExceptionInCaseOfEmptyURL(url);
            return Jsoup.connect(url).get();
        } catch (Exception e) {
            log.severe(String.format(ERROR_DURING_HTML_DOCUMENT_RETRIEVING, e.getMessage()));
            throw new HTMLDocumentRetrievingException(e);
        }
    }

    private static void throwExceptionInCaseOfEmptyURL(final String url) {
        if (isEmpty(url)) {
            final String exceptionMessage = String.format(NULL_OR_EMPTY_URL, url);
            log.severe(exceptionMessage);
            throw new InvalidURLException(exceptionMessage);
        }
    }
}
