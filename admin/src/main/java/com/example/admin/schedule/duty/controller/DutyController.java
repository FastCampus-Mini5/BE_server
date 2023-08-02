package com.example.admin.schedule.duty.controller;

import com.example.admin.schedule.duty.dto.DutyRequest;
import com.example.admin.schedule.duty.service.DutyService;
import com.example.core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/duty")
public class DutyController {

    private final DutyService dutyService;

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse.Result<String>> dutyApprove(
            @RequestBody @Valid DutyRequest.StatusDTO statusDTO, Error error) {
        dutyService.updateByStatus(statusDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
