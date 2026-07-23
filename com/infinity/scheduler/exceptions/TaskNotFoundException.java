package com.infinity.scheduler.exceptions;

/**
 * Thrown when a requested task cannot be found in the system database.
 */
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
