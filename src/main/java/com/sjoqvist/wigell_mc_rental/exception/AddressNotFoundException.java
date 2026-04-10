package com.sjoqvist.wigell_mc_rental.exception;

/**
 * This exception should be used when an address entity could not be found when retrieving them from
 * the address repository. An address id is always present and retrievable from this exception
 * class.
 */
public class AddressNotFoundException extends RuntimeException {
    private final Long addressId;

    public AddressNotFoundException(Long addressId) {
        super();
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

    /**
     * Convenience method for when you need an exception with a standard message about an address
     * that could not be found.
     *
     * @return Returns a {@link AddressNotFoundException} with a standard message about an address
     *     entity that could not be found.
     */
    public static AddressNotFoundException withNotFoundMsg(Long addressId) {
        return new AddressNotFoundException(
                "Address entity with id " + addressId + " not found", addressId);
    }

    public Long getAddressId() {
        return addressId;
    }
}
