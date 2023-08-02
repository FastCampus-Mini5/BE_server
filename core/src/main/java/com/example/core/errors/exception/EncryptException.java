package com.example.core.errors.exception;


import com.example.core.errors.ErrorMessage;

public class EncryptException extends RuntimeException {

    public EncryptException(String message) {
        super(ErrorMessage.ENCRYPT_ERROR + message);
    }
}
