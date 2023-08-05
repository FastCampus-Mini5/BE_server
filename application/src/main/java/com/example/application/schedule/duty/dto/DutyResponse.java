package com.example.application.schedule.duty.dto;

import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DutyResponse {

    @Getter
    @Builder
    public static class DutyDTO {

        private Long id;
        private String userEmail;
        private Timestamp dutyDate;
        private Status status;

        public static DutyDTO from(Duty duty) {
            return DutyDTO.builder()
                    .id(duty.getId())
                    .userEmail(duty.getUser().getEmail())
                    .dutyDate(duty.getDutyDate())
                    .status(duty.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MyDutyDTO {

        private Long id;
        private Timestamp dutyDate;
        private Status status;

        public static MyDutyDTO from(Duty duty) {
            return MyDutyDTO.builder()
                    .id(duty.getId())
                    .dutyDate(duty.getDutyDate())
                    .status(duty.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ListDTO {

        private String username;
        private String email;
        private Timestamp dutyDate;
        private Status status;

        public DutyResponse.ListDTO decrypt(Encryption encryption) {
            return ListDTO.builder()
                    .username(encryption.decrypt(username))
                    .email(encryption.decrypt(email))
                    .dutyDate(dutyDate)
                    .status(status)
                    .build();
        }

        public static ListDTO from(Duty duty) {
            return ListDTO.builder()
                    .username(duty.getUser().getUsername())
                    .email(duty.getUser().getEmail())
                    .dutyDate(duty.getDutyDate())
                    .status(duty.getStatus())
                    .build();
        }
    }
}