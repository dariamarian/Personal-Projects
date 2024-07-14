package com.example.gamificationapp.exceptions;

public class RepoException extends Exception {

    public RepoException() {
        super();
    }

    public RepoException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
