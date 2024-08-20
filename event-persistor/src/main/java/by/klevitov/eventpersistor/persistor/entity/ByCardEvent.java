package by.klevitov.eventpersistor.persistor.entity;

import by.klevitov.eventradarcommon.dto.EventPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

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
            this.priceStr = (this.priceStr == null ? sourceEvent.priceStr : priceStr);
            this.eventLink = (this.eventLink == null ? sourceEvent.eventLink : eventLink);
            this.imageLink = (this.imageLink == null ? sourceEvent.imageLink : imageLink);
            this.price = createUpdatedPrice(sourceEvent.price);
        }
    }

    private EventPrice createUpdatedPrice(final EventPrice source) {
        if (price == null) {
            return source;
        }
        this.price.setMinPrice(this.price.getMinPrice() == null
                ? source.getMinPrice() : price.getMinPrice());
        this.price.setMaxPrice(this.price.getMaxPrice() == null
                ? source.getMaxPrice() : price.getMaxPrice());
        return source;
    }
}
