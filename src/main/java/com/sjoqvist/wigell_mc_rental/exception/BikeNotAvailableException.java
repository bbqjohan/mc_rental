package com.sjoqvist.wigell_mc_rental.exception;

/**
 * This exception should be used when a bike entity could not be found when retrieving them from the
 * bike repository. An id is always present and retrievable from this exception class.
 */
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
