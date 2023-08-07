package com.example.application.schedule.vacation.dto;

import com.example.core.model.schedule.Reason;
import com.example.core.model.schedule.Vacation;
import com.example.core.model.user.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacationRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class AddDTO {

        @NotNull
        private Reason reason;

        @NotNull
        private Timestamp startDate;

        @NotNull
        private Timestamp endDate;

        public Vacation toEntityWith(User user) {
            return Vacation.builder()
                    .user(user)
                    .reason(reason)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }
    }
}