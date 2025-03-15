package com.danko.danko_handmade.web.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@UtilityClass
public class DateUtil {

    public String getFormattedDateWithSuffix(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");
        String formattedDate = date.format(formatter.withLocale(Locale.ENGLISH));
        String dayWithSuffix = addDaySuffix(date.getDayOfMonth());
        return formattedDate + dayWithSuffix;
    }

    private String addDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
