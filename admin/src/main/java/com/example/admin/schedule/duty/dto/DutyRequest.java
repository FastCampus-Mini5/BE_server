package com.example.admin.schedule.duty.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DutyRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDTO {

        private Long id;

        @NotBlank
        private String status;
    }
}
