package com.sjoqvist.wigell_mc_rental.exception;

public class BikeNotAvailableException extends RuntimeException {
    private final Long id;

    public BikeNotAvailableException(Long id) {
        super("The bike with ID " + id + " is not available");
        this.id = id;
    }

    public BikeNotAvailableException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public BikeNotAvailableException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
