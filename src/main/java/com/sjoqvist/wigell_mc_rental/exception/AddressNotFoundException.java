package com.sjoqvist.wigell_mc_rental.exception;

public class AddressNotFoundException extends RuntimeException {
    private final Long addressId;

    public AddressNotFoundException(Long addressId) {
        super("Address entity with id " + addressId + " not found");
        this.addressId = addressId;
    }

    public AddressNotFoundException(String message, Throwable cause, Long addressId) {
        super(message, cause);
        this.addressId = addressId;
    }

    public AddressNotFoundException(String message, Long addressId) {
        super(message);
        this.addressId = addressId;
    }

    public Long getAddressId() {
        return addressId;
    }
}
