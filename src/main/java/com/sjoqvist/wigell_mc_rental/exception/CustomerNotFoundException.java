package com.sjoqvist.wigell_mc_rental.exception;

/**
 * This exception should be used when a customer entity could not be found when retrieving them from
 * the customer repository. A customer id is always present and retrievable from this exception
 * class.
 */
public class CustomerNotFoundException extends RuntimeException {
    private final Long customerId;

    public CustomerNotFoundException(Long customerId) {
        super();
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

    /**
     * Convenience method for when you need an exception with a standard message about a customer
     * that could not be found.
     *
     * @return Returns a {@link CustomerNotFoundException} with a standard message about a customer
     *     entity that could not be found. Returns a {CustomerNotFoundException} with a standard
     *     message about a customer entity that could not * be found.
     */
    public static CustomerNotFoundException withNotFoundMsg(Long customerId) {
        return new CustomerNotFoundException(
                "Customer entity with id " + customerId + " not found", customerId);
    }

    public Long getCustomerId() {
        return customerId;
    }
}
