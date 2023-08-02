package com.example.core.errors.exception;


import com.example.core.errors.ErrorMessage;

public class DecryptException extends RuntimeException {

    public DecryptException(String message) {
        super(ErrorMessage.DECRYPT_ERROR + message);
    }
}
