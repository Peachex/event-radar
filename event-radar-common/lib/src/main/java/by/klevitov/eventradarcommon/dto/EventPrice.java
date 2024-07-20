package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPrice {
    private static final int PRICE_PARTS_NUMBER = 2;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
