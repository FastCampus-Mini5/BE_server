package com.example.admin.schedule.vacation.controller;

import com.example.admin.schedule.vacation.dto.VacationRequest;
import com.example.admin.schedule.vacation.service.VacationService;
import com.example.core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/vacation")
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("/proceed")
    public ResponseEntity<ApiResponse.Result<String>> vacationApprove(
            @RequestBody @Valid VacationRequest.StatusDTO statusDTO, Error error) {
        vacationService.updateStatus(statusDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
