package com.example.application.schedule.duty.dto;

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
    public static class ListDTO {

        private String username;
        private String email;
        private Timestamp dutyDate;

        public static ListDTO from(Duty duty) {
            return ListDTO.builder()
                    .username(duty.getUser().getUsername())
                    .email(duty.getUser().getEmail())
                    .dutyDate(duty.getDutyDate())
                    .build();
        }
    }
}