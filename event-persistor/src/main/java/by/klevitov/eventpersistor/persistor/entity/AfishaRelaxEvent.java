package by.klevitov.eventpersistor.persistor.entity;

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
        if (source instanceof AfishaRelaxEvent sourceEvent) {
            super.copyValuesForNullOrEmptyFieldsFromEvent(sourceEvent);
            eventLink = (isEmpty(eventLink) ? sourceEvent.eventLink : eventLink);
            imageLink = (isEmpty(imageLink) ? sourceEvent.imageLink : imageLink);
        }
    }
}
