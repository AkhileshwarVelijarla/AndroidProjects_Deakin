package com.example.a41_personaleventplannerapp.util;

public final class EventValidator {

    private EventValidator() {
    }

    public static boolean isTitleValid(String title) {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isDateSelected(Long dateTimeMillis) {
        return dateTimeMillis != null && dateTimeMillis > 0L;
    }

    public static boolean isDateInPast(long dateTimeMillis) {
        return dateTimeMillis < System.currentTimeMillis();
    }
}
