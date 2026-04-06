package com.example.a41_personaleventplannerapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTimeFormatterUtil {

    private static final SimpleDateFormat DISPLAY_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault());

    private DateTimeFormatterUtil() {
    }

    public static String format(long timeInMillis) {
        return DISPLAY_FORMAT.format(new Date(timeInMillis));
    }
}
