package com.example.core.errors.exception;

public class UserNotFoundException extends Exception500 {

    public UserNotFoundException(String message) {
        super(message);
    }
}
