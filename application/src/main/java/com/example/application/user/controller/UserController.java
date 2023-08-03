package com.example.application.user.controller;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.user.service.UserService;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.model.user.User;
import com.example.core.util.ApiResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
      HttpServletRequest request,
      @RequestBody @Valid UserRequest.SignInDTO signInDTO,
      Errors errors) {

    String jwt = userService.signIn(request, signInDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, jwt);

    log.info("{}", ResponseEntity.ok().headers(headers).body(null));

    return ResponseEntity.ok().headers(headers).body(ApiResponse.success());
  }

  @GetMapping("/emailCheck")
  public ResponseEntity<ApiResponse.Result<UserResponse.AvailableEmailDTO>> check(
      @ModelAttribute(name = "email") @Valid UserRequest.CheckEmailDTO checkEmailDTO,
      BindingResult bindingResult) {

    log.info("{}", checkEmailDTO);

    UserResponse.AvailableEmailDTO availableEmailDTO = userService.checkEmail(checkEmailDTO);

    return ResponseEntity.ok(ApiResponse.success(availableEmailDTO));
  }

  @GetMapping("/info")
  public ResponseEntity<ApiResponse.Result<UserResponse.UserInfoDTO>> userInfo(
      @AuthenticationPrincipal PrincipalUserDetail userDetail) {

    UserResponse.UserInfoDTO userInfoDTO =
        userService.getUserInfoByUserId(userDetail.getUser().getId());

    log.info("조회된 유저 정보 : {}", userInfoDTO);

    return ResponseEntity.ok(ApiResponse.success(userInfoDTO));
  }

  @PostMapping("/update")
  public ResponseEntity<ApiResponse.Result<String>> updateUserInfo(
      @AuthenticationPrincipal PrincipalUserDetail userDetail,
      @RequestBody @Valid UserRequest.UpdateInfoDTO updateInfoDTO,
      Errors errors) {

    userService.updateUserInfoByUserId(userDetail.getUser().getId(), updateInfoDTO);

    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PostMapping("/findPassword")
  public ResponseEntity<ApiResponse.Result<?>> findPassword(
      @RequestBody @Valid UserRequest.FindPasswordDTO findPasswordDTO, Errors errors) {
    log.info("/api/user/findPassword " + findPasswordDTO);

    userService.passwordReset(findPasswordDTO);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
