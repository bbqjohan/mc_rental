package com.sjoqvist.wigell_mc_rental.exception;

public class BookingNotFoundException extends RuntimeException {
    private final Long id;

    public BookingNotFoundException(Long id) {
        super("Booking entity with id " + id + " not found");
        this.id = id;
    }

    public BookingNotFoundException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public BookingNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
