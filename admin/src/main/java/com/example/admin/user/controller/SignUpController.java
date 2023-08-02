package com.example.admin.user.controller;

import com.example.admin.user.dto.SignUpRequest;
import com.example.admin.user.dto.SignUpResponse;
import com.example.admin.user.service.SignUpService;
import com.example.core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/signup")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse.Result<Object>> approve(
            @RequestBody @Valid SignUpRequest.ApproveDTO approveDTO,
            Errors errors) {
        log.info("POST /api/admin/signup/approve " + approveDTO);

        signUpService.approve(approveDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse.Result<Page<SignUpResponse.ListDTO>>> list(
            @PageableDefault(size = 5) Pageable pageable) {
        log.info("GET /api/admin/signup/list " + pageable);

        Page<SignUpResponse.ListDTO> SignUpResponse = signUpService.getAllList(pageable);
        return ResponseEntity.ok(ApiResponse.success(SignUpResponse));
    }
}
