package com.example.gamificationapp.exceptions;

public class ValidationException extends Exception {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
