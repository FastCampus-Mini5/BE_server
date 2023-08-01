package com.example.core.util;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {

    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.NO_CONTENT.value(), true, null, null);
    }

    public static <T> Result<T> success(T response) {
        return new Result<>(HttpStatus.OK.value(), true, response, null);
    }

    public static Result<Object> error(String message, HttpStatus status) {
        return new Result<>(status.value(), false, null, new Error(message));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Result<T> {
        private final int status;
        private final boolean success;
        private final T response;
        private final Error error;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Error {
        private final String message;
    }
}
