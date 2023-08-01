package com.example.core.errors.exception;

import com.example.core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/** internal server error exception */
@Getter
public class Exception500 extends RuntimeException {

  public Exception500(String message) {
    super(message);
  }

  public ApiResponse.Result<Object> body() {
    return ApiResponse.error(getMessage(), status());
  }

  public HttpStatus status() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
