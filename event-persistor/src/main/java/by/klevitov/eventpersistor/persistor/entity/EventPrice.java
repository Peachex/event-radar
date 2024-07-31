package by.klevitov.eventpersistor.persistor.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPrice {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    //todo Try replace this class with class from common library.
}
