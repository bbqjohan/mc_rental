package com.sjoqvist.wigell_mc_rental.exception;

/**
 * This exception should be used when a user entity could not be found when retrieving them from the
 * user repository. A username is always present and retrievable from this exception class.
 */
public class UserExistsException extends RuntimeException {
    private final String username;

    public UserExistsException(String username) {
        super();
        this.username = username;
    }

    public UserExistsException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }

    public UserExistsException(String message, String username) {
        super(message);
        this.username = username;
    }

    /**
     * Convenience method for when you need an exception with a standard message about a user that
     * already exists.
     *
     * @return Returns a {@link UserExistsException} with a standard message about a user entity
     *     that already exists.
     */
    public static UserExistsException withNotFoundMsg(String username) {
        return new UserExistsException(
                "Address entity with id " + username + " not found", username);
    }

    public String getUsername() {
        return username;
    }
}
