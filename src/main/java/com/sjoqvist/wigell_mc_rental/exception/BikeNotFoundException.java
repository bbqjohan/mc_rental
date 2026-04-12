package com.sjoqvist.wigell_mc_rental.exception;

public class BikeNotFoundException extends RuntimeException {
    private final Long id;

    public BikeNotFoundException(Long id) {
        super("Bike entity with id " + id + " not found");
        this.id = id;
    }

    public BikeNotFoundException(String message, Throwable cause, Long id) {
        super(message, cause);
        this.id = id;
    }

    public BikeNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
