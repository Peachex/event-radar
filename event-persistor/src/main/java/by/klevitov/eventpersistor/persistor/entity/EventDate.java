package by.klevitov.eventpersistor.persistor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDate {
    private LocalDate startDate;
    private LocalDate endDate;

    //todo Try replace this class with class from common library.
}
