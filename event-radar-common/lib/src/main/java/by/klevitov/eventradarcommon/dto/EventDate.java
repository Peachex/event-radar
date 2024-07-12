package by.klevitov.eventradarcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDate {
    private LocalDate startDate;
    private LocalDate endDate;

    public Duration calculateDuration() {
        return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
    }
}
