package com.gmail.maxdiland.drebedengireports.util.dateformat;

import com.gmail.maxdiland.drebedengireports.util.calendar.CalendarUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author Max Diland
 */
public final class DateTimeFormat {
    private DateTimeFormat() {}

    public static final DateFormat SQL_LITE_DETAILED =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.'000'SSS");

    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static final SimpleDateFormat SHORT_DATE_TIME
            = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    public static final SimpleDateFormat SHORT_DATE
            = new SimpleDateFormat("dd.MM.yyyy");

}