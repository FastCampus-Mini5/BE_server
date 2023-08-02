package com.example.application.user.service;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception500;
import com.example.core.model.serverLog.ServerLog;
import com.example.core.model.user.User;
import com.example.core.repository.serverLog.ServerLogRepository;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final SignUpRepository signUpRepository;
  private final UserRepository userRepository;
  private final ServerLogRepository serverLogRepository;
  private final Encryption encryption;

  public void saveSignUpRequest(UserRequest.SignUpDTO signUpDTO) {
    if (signUpDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNUP);

    signUpRepository.save(signUpDTO.toEntityEncrypted(passwordEncoder, encryption));
  }

  public String signIn(HttpServletRequest request, UserRequest.SignInDTO signInDTO) {
    if (signInDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNIN);

    String clientIP = getClientIP(request);

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(
            encryption.encrypt(signInDTO.getEmail()), signInDTO.getPassword());

    Authentication authentication = authenticationManager.authenticate(token);

    PrincipalUserDetail userDetail = (PrincipalUserDetail) authentication.getPrincipal();

    final User user = userDetail.getUser();

    logging(user, clientIP);

    return JwtTokenProvider.create(user);
  }

  private String getClientIP(HttpServletRequest request) {
    String[] headerNames = {
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

  private void logging(User user, String clientIp) {

    ServerLog serverLog =
        ServerLog.builder()
            .user(user)
            .requestIp(clientIp)
            .signInDate(Timestamp.valueOf(LocalDateTime.now()))
            .build();

    serverLogRepository.save(serverLog);
  }

  public UserResponse.AvailableEmailDTO checkEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
    if (checkEmailDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_CHECK_EMAIL);

    String plainEmail = checkEmailDTO.getEmail();
    String encryptedEmail = encryption.encrypt(checkEmailDTO.getEmail());

    if (userRepository.existsByEmail(encryptedEmail)) {
      return UserResponse.AvailableEmailDTO.builder().email(plainEmail).available(false).build();
    }

    return UserResponse.AvailableEmailDTO.builder().email(plainEmail).available(true).build();
  }

  public UserResponse.UserInfoDTO getUserInfoByUserId(Long userId) {

    User user = userRepository.getReferenceById(userId);

    return UserResponse.UserInfoDTO.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .profileImage(user.getProfileImage())
        .hireDate(user.getHireDate())
        .build()
        .toDecryptedDTO(encryption);
  }

  public void updateUserInfoByUserId(Long userId, UserRequest.UpdateInfoDTO updateInfoDTO) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> {
                  throw new Exception500(ErrorMessage.USER_NOT_FOUND);
                });

    String encryptedPassword = encryption.encrypt(updateInfoDTO.getPassword());

    user.setProfileImage(updateInfoDTO.getProfileImg());
    user.setPassword(encryptedPassword);

    userRepository.save(user);
  }
}
