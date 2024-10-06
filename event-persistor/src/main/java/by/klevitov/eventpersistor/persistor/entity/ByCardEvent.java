package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventpersistor.persistor.exception.InvalidEventClassException;
import by.klevitov.eventradarcommon.dto.EventPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import static by.klevitov.eventpersistor.persistor.constant.PersistorExceptionMessage.INVALID_EVENT_CLASS;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Document(collection = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ByCardEvent extends AbstractEvent {
    private String priceStr;
    private EventPrice price;
    private String eventLink;
    private String imageLink;

    @Override
    public void copyValuesForNullOrEmptyFieldsFromEvent(AbstractEvent source) {
        if (eventDoesNotBelongToThisClass(source)) {
            throw new InvalidEventClassException(String.format(INVALID_EVENT_CLASS, getClass(), source));
        }

        ByCardEvent event = (ByCardEvent) source;
        super.copyValuesForNullOrEmptyFieldsFromEvent(event);
        priceStr = (isEmpty(priceStr) ? event.priceStr : priceStr);
        eventLink = (isEmpty(eventLink) ? event.eventLink : eventLink);
        imageLink = (isEmpty(imageLink) ? event.imageLink : imageLink);
        price = createUpdatedPrice(event.price);
    }

    private boolean eventDoesNotBelongToThisClass(AbstractEvent event) {
        return !(event instanceof ByCardEvent);
    }

    private EventPrice createUpdatedPrice(final EventPrice source) {
        if (price == null) {
            return source;
        }
        price.setMinPrice(price.getMinPrice() == null ? source.getMinPrice() : price.getMinPrice());
        price.setMaxPrice(price.getMaxPrice() == null ? source.getMaxPrice() : price.getMaxPrice());
        return price;
    }
}
