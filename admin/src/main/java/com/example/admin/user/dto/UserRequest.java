package com.example.admin.user.dto;

import com.example.core.errors.ErrorMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SignInDTO {

        @NotBlank(message = ErrorMessage.EMPTY_DATA_TO_SIGNIN)
        private String email;

        @NotBlank(message = ErrorMessage.EMPTY_DATA_TO_SIGNIN)
        private String password;
    }
}
