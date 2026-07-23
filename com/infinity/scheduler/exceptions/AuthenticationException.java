package com.infinity.scheduler.exceptions;

/**
 * Thrown when an authentication or registration operation fails in the system.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
