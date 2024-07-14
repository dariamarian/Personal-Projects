package com.example.backend.exception;

public class AuthException extends Exception {

    public AuthException(String message) {
        super(message);
    }

    /**
     * This class represents the exception that is thrown when a user is not found.
     */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}
