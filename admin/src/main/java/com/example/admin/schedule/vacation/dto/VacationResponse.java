package com.example.admin.schedule.vacation.dto;

import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.schedule.Reason;
import com.example.core.model.schedule.Vacation;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacationResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListDTO {

        private Long id;
        private String username;
        private String email;
        private Reason reason;
        private Timestamp createdAt;
        private Timestamp startDate;
        private Timestamp endDate;

        public ListDTO decrypt(Encryption encryption) {
            return ListDTO.builder()
                    .id(id)
                    .username(encryption.decrypt(username))
                    .email(encryption.decrypt(email))
                    .reason(reason)
                    .createdAt(createdAt)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }

        public static ListDTO form(Vacation vacation) {
            return ListDTO.builder()
                    .id(vacation.getId())
                    .username(vacation.getUser().getUsername())
                    .email(vacation.getUser().getEmail())
                    .reason(vacation.getReason())
                    .createdAt(vacation.getCreatedDate())
                    .startDate(vacation.getStartDate())
                    .endDate(vacation.getEndDate())
                    .build();
        }
    }
}
