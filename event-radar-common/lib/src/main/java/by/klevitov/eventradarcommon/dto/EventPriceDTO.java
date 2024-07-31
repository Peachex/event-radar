package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPriceDTO {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
