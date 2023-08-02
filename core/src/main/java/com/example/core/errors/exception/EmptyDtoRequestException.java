package com.example.core.errors.exception;

public class EmptyDtoRequestException extends RuntimeException {

    public EmptyDtoRequestException(String message) {
        super(message);
    }
}
