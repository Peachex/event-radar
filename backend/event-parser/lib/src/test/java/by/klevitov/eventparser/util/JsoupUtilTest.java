package by.klevitov.eventparser.util;

import by.klevitov.eventparser.exception.HTMLDocumentRetrievingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static by.klevitov.eventparser.util.JsoupUtil.retrieveDocumentByURL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsoupUtilTest {
    @ParameterizedTest
    @ValueSource(strings = {"https://afisha.relax.by", "https://bycard.by"})
    public void test_retrieveDocumentByURL_withValidURL(String url) {
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
        assertThrows(HTMLDocumentRetrievingException.class, () -> retrieveDocumentByURL(url));
    }

    @Test
    public void test_retrieveDocumentByURL_withEmptyURL() {
        String url = "";
        assertThrows(HTMLDocumentRetrievingException.class, () -> retrieveDocumentByURL(url));
    }
}
