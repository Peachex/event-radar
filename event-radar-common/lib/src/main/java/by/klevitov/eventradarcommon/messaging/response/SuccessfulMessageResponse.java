package by.klevitov.eventradarcommon.messaging.response;

import by.klevitov.eventradarcommon.messaging.request.EntityData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class SuccessfulMessageResponse extends MessageResponse {
    private EntityData entityData;

    public SuccessfulMessageResponse(String id, String requestId, LocalDateTime requestCreatedDate,
                                     LocalDateTime responseCreatedDate, boolean hasError) {
        super(id, requestId, requestCreatedDate, responseCreatedDate, hasError);
    }

    public SuccessfulMessageResponse(boolean hasError) {
        super(hasError);
    }

    public SuccessfulMessageResponse(String requestId, LocalDateTime requestCreatedDate,
                                     EntityData entityData) {
        super(requestId, requestCreatedDate);
        this.entityData = entityData;
    }

    public SuccessfulMessageResponse(EntityData entityData) {
        super();
        this.entityData = entityData;
    }

    //todo Delete redundant constructors.
}
