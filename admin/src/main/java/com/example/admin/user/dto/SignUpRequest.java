package com.example.admin.user.dto;

import com.example.core.errors.ErrorMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ApproveDTO {

        @NotBlank(message = ErrorMessage.EMPTY_DATA_TO_APPROVE_SIGNUP)
        private String email;
    }
}
