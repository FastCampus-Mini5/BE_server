package com.example.admin.schedule.vacation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacationRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusDTO {
        private Long id;

        @NotBlank
        private String status;
    }
}

