package by.klevitov.eventpersistor.messaging.comnon.response.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorMessageResponse extends MessageResponse {
    private String errorMessage;
    private Throwable throwable;

    public ErrorMessageResponse(String errorMessage, Throwable throwable) {
        super(true);
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }

    public ErrorMessageResponse(String id, String requestId, LocalDateTime requestCreatedDate, String errorMessage,
                                Throwable throwable) {
        this(id, requestId, requestCreatedDate);
        this.errorMessage = errorMessage;
        this.throwable = throwable;
    }

    public ErrorMessageResponse(String id, String requestId, LocalDateTime requestCreatedDate) {
        super(id, requestId, requestCreatedDate, now(), true);
    }
}
