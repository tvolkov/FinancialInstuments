package com.infusion.calculation;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateValidator {
    private final LocalDate treshold;

    public DateValidator(LocalDate treshold){
        this.treshold = treshold;
    }

    public boolean isDateValid(LocalDate date){
        return isBusinessDay(date) && isDateBeforeToday(date);
    }

    private boolean isDateBeforeToday(LocalDate date) {
        return date.isBefore(treshold);
    }

    private boolean isBusinessDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
