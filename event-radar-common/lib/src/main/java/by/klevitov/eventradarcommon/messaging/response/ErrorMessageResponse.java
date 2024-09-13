package by.klevitov.eventradarcommon.messaging.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@NoArgsConstructor
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

    //todo Delete redundant constructors.
}
