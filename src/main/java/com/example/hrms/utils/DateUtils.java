package com.example.hrms.utils;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    }
}

