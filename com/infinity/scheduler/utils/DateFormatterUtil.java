package com.infinity.scheduler.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for converting Date and Time objects to and from readable
 * Strings.
 */
public class DateFormatterUtil {

    private static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter SYSTEM_DATE_FORMATTER = DateTimeFormatter
            .ofPattern(STANDARD_DATE_TIME_FORMAT);

    /**
     * Converts a LocalDateTime object into a standardized string.
     */
    public static String formatDateTimeToString(LocalDateTime targetDateTime) {
        if (targetDateTime == null) {
            return "No Date Assigned";
        }
        return targetDateTime.format(SYSTEM_DATE_FORMATTER);
    }

    /**
     * Parses a standardized string back into a LocalDateTime object.
     */
    public static LocalDateTime parseStringToDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, SYSTEM_DATE_FORMATTER);
        } catch (DateTimeParseException parsingException) {
            System.err.println("⚠️ Invalid date format. Please use: " + STANDARD_DATE_TIME_FORMAT);
            return null;
        }
    }
}