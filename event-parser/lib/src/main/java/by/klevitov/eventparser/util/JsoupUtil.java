package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import by.klevitov.eventparser.exception.InvalidURLException;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static by.klevitov.eventparser.constant.ExceptionMessage.NULL_OR_EMPTY_URL;
import static by.klevitov.eventparser.constant.ExceptionMessage.ERROR_DURING_HTML_DOCUMENT_RETRIEVING;

@Log4j2
public final class JsoupUtil {
    private JsoupUtil() {
    }

    public static Document retrieveDocumentByURL(final String url) throws HTMLDocumentRetrievingException {
        try {
            throwExceptionInCaseOfEmptyURL(url);
            return Jsoup.connect(url).get();
        } catch (Exception e) {
            log.error(String.format(ERROR_DURING_HTML_DOCUMENT_RETRIEVING, e.getMessage()));
            throw new HTMLDocumentRetrievingException(e);
        }
    }

    private static void throwExceptionInCaseOfEmptyURL(final String url) {
        if (isEmpty(url)) {
            final String exceptionMessage = String.format(NULL_OR_EMPTY_URL, url);
            log.error(exceptionMessage);
            throw new InvalidURLException(exceptionMessage);
        }
    }
}
