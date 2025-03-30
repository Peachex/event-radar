package by.klevitov.eventpersistor.entity;

import by.klevitov.eventpersistor.constant.PersistorExceptionMessage;
import by.klevitov.eventpersistor.exception.InvalidEventClassException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Document(collection = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class AfishaRelaxEvent extends AbstractEvent {
    private String eventLink;
    private String imageLink;

    @Override
    public void copyValuesForNullOrEmptyFieldsFromEvent(AbstractEvent source) {
        if (eventDoesNotBelongToThisClass(source)) {
            throw new InvalidEventClassException(String.format(PersistorExceptionMessage.INVALID_EVENT_CLASS, getClass(), source));
        }

        AfishaRelaxEvent event = (AfishaRelaxEvent) source;
        super.copyValuesForNullOrEmptyFieldsFromEvent(event);
        eventLink = (isEmpty(eventLink) ? event.eventLink : eventLink);
        imageLink = (isEmpty(imageLink) ? event.imageLink : imageLink);
    }

    private boolean eventDoesNotBelongToThisClass(AbstractEvent event) {
        return !(event instanceof AfishaRelaxEvent);
    }
}
