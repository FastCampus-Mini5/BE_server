package com.example.core.errors.exception;

import com.example.core.errors.ErrorMessage;

public class MailSendFailureException extends Exception500 {

    public MailSendFailureException() {
        super(ErrorMessage.EMAIL_SEND_FAILED);
    }
}
