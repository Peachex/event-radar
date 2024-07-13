package by.klevitov.eventparser;

import by.klevitov.eventparser.util.EventParserUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.Optional;

public class Main {
    //todo delete class.
    public static void main(String[] args) {
        String singleDate = "12 августа";
        String dualDate = "12 September - 18 September";
        String startDate = "с 23 September";
        String endDate = "до 12 августа";

        String dualDate1 = "12 августа - 18 сентября";

        Pair<LocalDate, LocalDate> localDate1 = EventParserUtil.convertDateToLocalDate(dualDate, null);
        System.out.println(localDate1);

        Pair<LocalDate, LocalDate> localDate2 = EventParserUtil.convertDateToLocalDate(dualDate1, null);
        System.out.println(localDate2);

//        Pair<LocalDate, LocalDate> localDate3 = EventParserUtil.convertDateToLocalDate(endDate, null);
//        System.out.println(localDate3);
    }
}
