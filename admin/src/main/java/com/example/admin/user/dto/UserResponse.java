package com.example.admin.user.dto;

import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.schedule.VacationInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

public class UserResponse {

    @Getter
    @Builder
    public static class ListDTO {

        private String username;
        private String email;
        private Timestamp hireDate;
        private int remainVacation;

        public static ListDTO from(VacationInfo vacationInfo) {
            return ListDTO.builder()
                    .username(vacationInfo.getUser().getUsername())
                    .email(vacationInfo.getUser().getEmail())
                    .hireDate(vacationInfo.getUser().getHireDate())
                    .remainVacation(vacationInfo.getRemainVacation())
                    .build();
        }

        public ListDTO decrypt(Encryption encryption) {
            return ListDTO.builder()
                    .username(encryption.decrypt(username))
                    .email(encryption.decrypt(email))
                    .hireDate(hireDate)
                    .remainVacation(remainVacation)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SignInDTO {

        private String jwt;
    }
}
