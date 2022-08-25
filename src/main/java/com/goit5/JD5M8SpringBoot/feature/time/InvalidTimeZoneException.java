package com.goit5.JD5M8SpringBoot.feature.time;

public class InvalidTimeZoneException extends RuntimeException {
    public InvalidTimeZoneException(String timezone) {
        super("Invalid timezone: " + timezone);
    }
}