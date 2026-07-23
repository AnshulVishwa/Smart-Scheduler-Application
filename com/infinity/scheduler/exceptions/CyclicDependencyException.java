package com.infinity.scheduler.exceptions;

/**
 * Thrown when establishing a task dependency would create a circular/cyclic workflow dependency.
 */
public class CyclicDependencyException extends Exception {

    public CyclicDependencyException(String message) {
        super(message);
    }

    public CyclicDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
