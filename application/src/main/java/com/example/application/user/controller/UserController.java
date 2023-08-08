package com.example.application.user.controller;

import static com.example.core.config._security.jwt.JwtTokenProvider.TOKEN_PREFIX;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.user.service.LoggingService;
import com.example.application.user.service.MailService;
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
  private final LoggingService loggingService;
  private final MailService mailService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse.Result<User>> signup(
      @RequestBody @Valid UserRequest.SignUpDTO signUpDTO, Errors errors) {

    userService.saveSignUpRequest(signUpDTO);

    return ResponseEntity.ok(ApiResponse.success());
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse.Result<String>> signIn(
      HttpServletRequest request,
      @RequestBody @Valid UserRequest.SignInDTO signInDTO,
      Errors errors) {

    String jwt = userService.signIn(signInDTO);

    if (jwt.startsWith(TOKEN_PREFIX))
      loggingService.logging(signInDTO.getEmail(), getClientIP(request));

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, jwt);

    log.info("{}", ResponseEntity.ok().headers(headers).body(ApiResponse.success()));

    return ResponseEntity.ok().headers(headers).body(ApiResponse.success());
  }

  private String getClientIP(HttpServletRequest request) {
    final String[] headerNames = {
      "X-Forwarded-For",
      "Proxy-Client-IP",
      "WL-Proxy-Client-IP",
      "HTTP_CLIENT_IP",
      "HTTP_X_FORWARDED_FOR"
    };

    for (String header : headerNames) {
      String ipAddress = request.getHeader(header);
      if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
        // 일부 프록시들은 쉼표로 구분된 IP 주소 목록을 사용하며, 실제 클라이언트 IP는 첫 번째로 옵니다.
        if (ipAddress.contains(",")) {
          return ipAddress.split(",")[0].trim();
        }
        return ipAddress;
      }
    }

    return request.getRemoteAddr();
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

    return ResponseEntity.ok(ApiResponse.success());
  }

  @PostMapping("/findPassword")
  public ResponseEntity<ApiResponse.Result<?>> findPassword(
      @RequestBody @Valid UserRequest.ResetPasswordDTO resetPasswordDTO, Errors errors) {
    log.info("/api/user/findPassword " + resetPasswordDTO);

    String tempPassword = userService.resetPassword(resetPasswordDTO);
    mailService.send(resetPasswordDTO.getEmail(), tempPassword);

    return ResponseEntity.ok(ApiResponse.success());
  }
}
