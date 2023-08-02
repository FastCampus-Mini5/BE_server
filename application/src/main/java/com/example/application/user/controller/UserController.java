package com.example.application.user.controller;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.user.service.UserService;
import com.example.core.util.ApiResponse;
import com.example.core.model.user.User;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse.Result<User>> signup(
      @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, Errors errors) {

    userService.saveSignUpRequest(signUpDTO);

    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse.Result<String>> signIn(
      @RequestBody @Valid UserRequest.SignInDTO signInDTO, Errors errors) {

    String jwt = userService.signIn(signInDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, jwt);

    log.info("{}", ResponseEntity.ok().headers(headers).body(null));

    return ResponseEntity.ok().headers(headers).body(null);
  }

  @GetMapping("/emailCheck")
  public ResponseEntity<ApiResponse.Result<UserResponse.AvailableEmailDTO>> check(
      @ModelAttribute(name = "email") @Valid UserRequest.CheckEmailDTO checkEmailDTO,
      BindingResult bindingResult) {

    log.info("{}", checkEmailDTO);

    UserResponse.AvailableEmailDTO availableEmailDTO = userService.checkEmail(checkEmailDTO);

    return ResponseEntity.ok(ApiResponse.success(availableEmailDTO));
  }
}
