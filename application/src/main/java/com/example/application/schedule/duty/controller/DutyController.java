package com.example.application.schedule.duty.controller;

import com.example.application.schedule.duty.dto.DutyRequest;
import com.example.application.schedule.duty.dto.DutyResponse;
import com.example.application.schedule.duty.service.DutyService;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/duty")
public class DutyController {

    private final DutyService dutyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse.Result<DutyResponse.DutyDTO>> add(@RequestBody @Valid DutyRequest.AddDTO dutyRequest,
                                                                        @AuthenticationPrincipal PrincipalUserDetail userDetails) {

        Long userId = userDetails.getUser().getId();
        DutyResponse.DutyDTO dutyResponse = dutyService.requestDuty(dutyRequest,userId);

        return ResponseEntity.ok(ApiResponse.success(dutyResponse));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse.Result<DutyResponse.DutyDTO>> cancel(@RequestBody @Valid DutyRequest.CancelDTO cancelDTO,
                                                                           @AuthenticationPrincipal PrincipalUserDetail userDetails) {

        Long userId = userDetails.getUser().getId();
        DutyResponse.DutyDTO cancelledDuty = dutyService.cancelDuty(cancelDTO, userId);

        return ResponseEntity.ok(ApiResponse.success(cancelledDuty));
    }

    @GetMapping("/all/list")
    public ResponseEntity<ApiResponse.Result<Page<DutyResponse.ListDTO>>> getAllDutiesByYear(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam("year") int year) {
        log.info("GET /api/user/duty/all/list " + year);

        Page<DutyResponse.ListDTO> listResponse = dutyService.getAllDutiesByYear(year, pageable);
        return ResponseEntity.ok(ApiResponse.success(listResponse));
    }
}