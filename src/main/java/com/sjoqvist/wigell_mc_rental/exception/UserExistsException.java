package com.sjoqvist.wigell_mc_rental.exception;

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

    public String getUsername() {
        return username;
    }
}
