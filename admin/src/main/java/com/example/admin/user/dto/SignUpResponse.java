package com.example.admin.user.dto;

import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.user.SignUp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpResponse {

    @Getter
    @Builder
    public static class ListDTO {

        private String username;
        private String email;
        private Timestamp hireDate;

        public static ListDTO from(SignUp signUp) {
            return ListDTO.builder()
                    .username(signUp.getUsername())
                    .email(signUp.getEmail())
                    .hireDate(signUp.getHireDate())
                    .build();
        }

        public ListDTO decrypt(Encryption encryption) {
            return ListDTO.builder()
                    .username(encryption.decrypt(username))
                    .email(encryption.decrypt(email))
                    .hireDate(hireDate)
                    .build();
        }
    }
}
