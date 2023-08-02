package com.example.application.schedule.vacation.controller;

import com.example.application.schedule.vacation.dto.VacationRequest;
import com.example.application.schedule.vacation.dto.VacationResponse;
import com.example.application.schedule.vacation.service.VacationService;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("/vacation/add")
    public ResponseEntity<ApiResponse.Result<VacationResponse.VacationDTO>> add(@RequestBody @Valid VacationRequest.AddDTO vacationRequest,
                                                                                @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        Long userId = userDetails.getUser().getId();
        VacationResponse.VacationDTO vacationDTO = vacationService.requestVacation(vacationRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(vacationDTO));
    }

    @DeleteMapping("/vacation/cancel")
    public ResponseEntity<ApiResponse.Result<VacationResponse.VacationDTO>> cancel(@RequestBody @Valid VacationRequest.CancelDTO cancelDTO,
                                                                                   @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        Long userId = userDetails.getUser().getId();
        VacationResponse.VacationDTO cancelledVacation = vacationService.cancelVacation(cancelDTO, userId);

        return ResponseEntity.ok(ApiResponse.success(cancelledVacation));
    }
}