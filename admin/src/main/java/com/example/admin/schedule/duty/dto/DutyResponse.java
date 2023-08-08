package com.example.admin.schedule.duty.dto;

import com.example.admin.schedule.vacation.dto.VacationResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.schedule.Duty;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DutyResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListDTO {

        private Long id;
        private String username;
        private String email;
        private Timestamp dutyDate;
        private Timestamp createdDate;

        public DutyResponse.ListDTO decrypt(Encryption encryption) {
            return ListDTO.builder()
                    .id(id)
                    .username(encryption.decrypt(username))
                    .email(encryption.decrypt(email))
                    .dutyDate(dutyDate)
                    .createdDate(createdDate)
                    .build();
        }

        public static ListDTO form(Duty duty) {
            return ListDTO.builder()
                    .id(duty.getId())
                    .username(duty.getUser().getUsername())
                    .email(duty.getUser().getEmail())
                    .dutyDate(duty.getDutyDate())
                    .createdDate(duty.getCreatedDate())
                    .build();
        }
    }
}
