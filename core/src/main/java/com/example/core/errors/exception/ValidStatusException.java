package com.example.core.errors.exception;

import com.example.core.errors.ErrorMessage;

public class ValidStatusException extends RuntimeException {

  public ValidStatusException() {
    super(ErrorMessage.INVALID_STATUS);
  }
}
