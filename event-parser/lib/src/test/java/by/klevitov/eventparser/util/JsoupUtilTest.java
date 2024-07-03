package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import org.junit.jupiter.api.Test;

import static by.klevitov.eventparser.util.JsoupUtil.retrieveDocumentByURL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsoupUtilTest {
    @Test
    public void test_retrieveDocumentByURL_withValidURL() {
        String url = "https://afisha.relax.by";
        assertDoesNotThrow(() -> {
            retrieveDocumentByURL(url);
        });
    }

    @Test
    public void test_retrieveDocumentByURL_withInvalidURL() {
        String url = "invalid_url";
        assertThrows(HTMLDocumentRetrievingException.class, () -> retrieveDocumentByURL(url));
    }

    @Test
    public void test_retrieveDocumentByURL_withNullURL() {
        String url = null;
        Exception exception = assertThrows(HTMLDocumentRetrievingException.class, () -> {
            retrieveDocumentByURL(url);
        });
        String expectedMessage = "by.klevitov.eventparser.exception.InvalidURLException: URL cannot be null or " +
                "empty: null";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_retrieveDocumentByURL_withEmptyURL() {
        String url = "";
        Exception exception = assertThrows(HTMLDocumentRetrievingException.class, () -> {
            retrieveDocumentByURL(url);
        });
        String expectedMessage = "by.klevitov.eventparser.exception.InvalidURLException: URL cannot be null or empty: ";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
