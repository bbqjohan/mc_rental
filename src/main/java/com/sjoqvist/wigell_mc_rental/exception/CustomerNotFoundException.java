package com.sjoqvist.wigell_mc_rental.exception;

public class CustomerNotFoundException extends RuntimeException {
    private final Long customerId;

    public CustomerNotFoundException(Long customerId) {
        super("Customer entity with id " + customerId + " not found");
        this.customerId = customerId;
    }

    public CustomerNotFoundException(String message, Throwable cause, Long customerId) {
        super(message, cause);
        this.customerId = customerId;
    }

    public CustomerNotFoundException(String message, Long customerId) {
        super(message);
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
