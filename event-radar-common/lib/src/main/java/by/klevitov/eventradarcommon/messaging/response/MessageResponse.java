package by.klevitov.eventradarcommon.messaging.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static by.klevitov.eventradarcommon.util.IdGenerator.generateId;
import static java.time.LocalDateTime.now;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SuccessfulMessageResponse.class, name = "successfulMessageResponse"),
        @JsonSubTypes.Type(value = ErrorMessageResponse.class, name = "errorMessageResponse")
})

@Data
@AllArgsConstructor
public abstract class MessageResponse {
    private String id;
    private String requestId;
    private LocalDateTime requestCreatedDate;
    private LocalDateTime responseCreatedDate;
    private boolean hasError;

    public MessageResponse() {
        this.id = generateId();
        this.responseCreatedDate = requestCreatedDate;
        this.responseCreatedDate = now();
        this.hasError = false;
    }

    public MessageResponse(boolean hasError) {
        this.id = generateId();
        this.responseCreatedDate = now();
        this.hasError = hasError;
    }

    public MessageResponse(String requestId, LocalDateTime requestCreatedDate) {
        this.id = generateId();
        this.requestId = requestId;
        this.responseCreatedDate = requestCreatedDate;
    }

    //todo Delete redundant constructors.
}
