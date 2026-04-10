package com.sjoqvist.wigell_mc_rental.exception;

/**
 * This exception should be used when a user entity could not be found when retrieving them from the
 * user repository. A username is always present and retrievable from this exception class.
 */
public class BikeExistsException extends RuntimeException {
    private final String model;
    private final String manufacturer;

    public BikeExistsException(String model, String manufacturer) {
        super(
                String.format(
                        "Bike with model %s and manufacturer %s already exists",
                        model, manufacturer));
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public BikeExistsException(String message, Throwable cause, String model, String manufacturer) {
        super(message, cause);
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public BikeExistsException(String message, String model, String manufacturer) {
        super(message);
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
