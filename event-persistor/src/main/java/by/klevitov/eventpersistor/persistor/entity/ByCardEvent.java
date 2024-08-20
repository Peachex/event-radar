package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventradarcommon.dto.EventPrice;
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
public class ByCardEvent extends AbstractEvent {
    private String priceStr;
    private EventPrice price;
    private String eventLink;
    private String imageLink;

    @Override
    public void copyValuesForNullOrEmptyFieldsFromEvent(AbstractEvent source) {
        if (source instanceof ByCardEvent sourceEvent) {
            super.copyValuesForNullOrEmptyFieldsFromEvent(sourceEvent);
            priceStr = (isEmpty(priceStr) ? sourceEvent.priceStr : priceStr);
            eventLink = (isEmpty(eventLink) ? sourceEvent.eventLink : eventLink);
            imageLink = (isEmpty(imageLink) ? sourceEvent.imageLink : imageLink);
            price = createUpdatedPrice(sourceEvent.price);
        }
    }

    private EventPrice createUpdatedPrice(final EventPrice source) {
        if (price == null) {
            return source;
        }
        price.setMinPrice(price.getMinPrice() == null ? source.getMinPrice() : price.getMinPrice());
        price.setMaxPrice(price.getMaxPrice() == null ? source.getMaxPrice() : price.getMaxPrice());
        return source;
    }
}
