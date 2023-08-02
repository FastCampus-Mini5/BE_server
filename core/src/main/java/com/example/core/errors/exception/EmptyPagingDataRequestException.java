package com.example.core.errors.exception;

import com.example.core.errors.ErrorMessage;

public class EmptyPagingDataRequestException extends RuntimeException {

    public EmptyPagingDataRequestException() {
        super(ErrorMessage.EMPTY_DATA_TO_PAGING);
    }
}
