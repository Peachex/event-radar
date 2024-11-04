package by.klevitov.eventradarcommon.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "timestamp")
@ToString
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
        this.rootStackTrace = findRootStackTrace(throwable);
    }

    private String findRootStackTrace(final Throwable throwable) {
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        return (stackTraceElements.length > 0 ? stackTraceElements[0].toString() : EMPTY);
    }
}
