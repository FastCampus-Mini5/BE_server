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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/vacation")
public class VacationController {

    private final VacationService vacationService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse.Result<VacationResponse.VacationDTO>> add(@RequestBody @Valid VacationRequest.AddDTO vacationRequest,
                                                                                @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        Long userId = userDetails.getUser().getId();
        VacationResponse.VacationDTO vacationDTO = vacationService.requestVacation(vacationRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(vacationDTO));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse.Result<VacationResponse.VacationDTO>> cancel(@RequestParam Long id,
                                                                                   @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        Long userId = userDetails.getUser().getId();
        VacationResponse.VacationDTO cancelledVacation = vacationService.cancelVacation(id, userId);

        return ResponseEntity.ok(ApiResponse.success(cancelledVacation));
    }

    @GetMapping("/myvacation")
    public ResponseEntity<ApiResponse.Result<List<VacationResponse.MyVacationDTO>>> getMyVacationsByYear(
            @RequestParam("year") int year,
            @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        log.info("GET /api/user/vacation/myvacation " + year);

        Long userId = userDetails.getUser().getId();
        List<VacationResponse.MyVacationDTO> myVacationResponse = vacationService.getMyVacationsByYear(year, userId);
        return ResponseEntity.ok(ApiResponse.success(myVacationResponse));
    }

    @GetMapping("/all/list")
    public ResponseEntity<ApiResponse.Result<List<VacationResponse.ListDTO>>> getAllVacationsByYear(
            @RequestParam("year") int year) {
        log.info("GET /api/user/vacation/all/list " + year);

        List<VacationResponse.ListDTO> listResponse = vacationService.getAllVacationsByYear(year);
        return ResponseEntity.ok(ApiResponse.success(listResponse));
    }
}