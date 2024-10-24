package by.klevitov.eventpersistor.persistor.web.handler;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {
    private LocalDateTime timestamp;
    private int status;
    private String exceptionMessage;
    private String exceptionClass;
    private String rootStackTrace;

    public ExceptionResponse(int status, Throwable throwable) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.exceptionMessage = throwable.getMessage();
        this.exceptionClass = throwable.getClass().getName();
        this.rootStackTrace = throwable.getStackTrace()[0].toString();
    }

    private String findRootStackTrace(final Throwable throwable) {
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        return (stackTraceElements.length > 0 ? stackTraceElements[0].toString() : StringUtils.EMPTY);
    }
}
