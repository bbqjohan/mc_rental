package com.sjoqvist.wigell_mc_rental.exception;

import java.time.LocalDate;

public class InvalidBookingDateException extends RuntimeException {
    private final LocalDate date;

    public InvalidBookingDateException(String message, Throwable cause, LocalDate date) {
        super(message, cause);
        this.date = date;
    }

    public InvalidBookingDateException(String message, LocalDate date) {
        super(message);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
