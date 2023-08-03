package com.example.admin.user.controller;

import com.example.admin.user.dto.UserRequest;
import com.example.admin.user.dto.UserResponse;
import com.example.admin.user.service.UserService;
import com.example.core.config._security.jwt.JwtTokenProvider;
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
@RequestMapping("/api/admin/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse.Result<Object>> list(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("GET /api/admin/user/list " + pageable);

        Page<UserResponse.ListDTO> listResponse = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(listResponse));
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse.Result<Object>> signIn(
            @RequestBody @Valid UserRequest.SignInDTO signInDTO,
            Errors errors) {
        log.info("POST /api/admin/user/list " + signInDTO);

        UserResponse.SignInDTO signInResponse = userService.signIn(signInDTO);
        return ResponseEntity.ok()
                .header(JwtTokenProvider.HEADER, signInResponse.getJwt())
                .body(ApiResponse.success());}
}
