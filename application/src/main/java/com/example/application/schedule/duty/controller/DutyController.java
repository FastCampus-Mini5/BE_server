package com.example.application.schedule.duty.controller;

import com.example.application.schedule.duty.dto.DutyRequest;
import com.example.application.schedule.duty.dto.DutyResponse;
import com.example.application.schedule.duty.service.DutyService;
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
    public ResponseEntity<ApiResponse.Result<DutyResponse.DutyDTO>> cancel(@RequestParam Long id,
                                                                           @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        Long userId = userDetails.getUser().getId();
        DutyResponse.DutyDTO cancelledDuty = dutyService.cancelDuty(id, userId);
        return ResponseEntity.ok(ApiResponse.success(cancelledDuty));
    }

    @GetMapping("/myduty")
    public ResponseEntity<ApiResponse.Result<List<DutyResponse.MyDutyDTO>>> getMyDutiesByYear(
            @RequestParam("year") int year,
            @AuthenticationPrincipal PrincipalUserDetail userDetails) {
        log.info("GET /api/user/duty/myduty " + year);

        Long userId = userDetails.getUser().getId();
        List<DutyResponse.MyDutyDTO> myDutyResponse = dutyService.getMyDutiesByYear(year, userId);
        return ResponseEntity.ok(ApiResponse.success(myDutyResponse));
    }

    @GetMapping("/all/list")
    public ResponseEntity<ApiResponse.Result<List<DutyResponse.ListDTO>>> getAllDutiesByYear(
            @RequestParam("year") int year) {
        log.info("GET /api/user/duty/all/list " + year);

        List<DutyResponse.ListDTO> listResponse = dutyService.getAllDutiesByYear(year);
        return ResponseEntity.ok(ApiResponse.success(listResponse));
    }
}