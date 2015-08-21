package com.gmail.maxdiland.drebedengireports.util.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * author Max Diland
 */
public final class CalendarUtil {
    private CalendarUtil() {}

    public static void putTheClockToTheBeginningOfTheDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
