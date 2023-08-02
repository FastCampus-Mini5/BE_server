package com.example.application.schedule.vacation.dto;

import com.example.core.model.schedule.Reason;
import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacationResponse {

    @Getter
    @Builder
    public static class VacationDTO {

        private Long id;
        private String userEmail;
        private String username;
        private Reason reason;
        private Status status;
        private Timestamp startDate;
        private Timestamp endDate;
        private Timestamp approvalDate;

        public static VacationDTO from(Vacation vacation) {
            return VacationDTO.builder()
                    .id(vacation.getId())
                    .userEmail(vacation.getUser().getEmail())
                    .username(vacation.getUser().getUsername())
                    .reason(vacation.getReason())
                    .status(vacation.getStatus())
                    .startDate(vacation.getStartDate())
                    .endDate(vacation.getEndDate())
                    .approvalDate(vacation.getApprovalDate())
                    .build();
        }
    }
}