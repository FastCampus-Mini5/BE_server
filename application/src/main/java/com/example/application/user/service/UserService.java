package com.example.application.user.service;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception500;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final SignUpRepository signUpRepository;
  private final UserRepository userRepository;
  private final Encryption encryption;

  public void saveSignUpRequest(UserRequest.SignUpDTO signUpDTO) {
    if (signUpDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNUP);

    signUpRepository.save(signUpDTO.toEntityEncrypted(passwordEncoder, encryption));
  }

  public String signIn(UserRequest.SignInDTO signInDTO) {
    if (signInDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNIN);

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword());

    Authentication authentication = authenticationManager.authenticate(token);
    PrincipalUserDetail userDetail = (PrincipalUserDetail) authentication.getPrincipal();
    final User user = userDetail.getUser();

    return JwtTokenProvider.create(user);
  }

  public UserResponse.AvailableEmailDTO checkEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
    if (checkEmailDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_CHECK_EMAIL);

    String encryptedEmail = encryption.encrypt(checkEmailDTO.getEmail());

    if (userRepository.existsByEmail(encryptedEmail)) {
      return UserResponse.AvailableEmailDTO.builder()
          .email(encryptedEmail)
          .available(false)
          .build();
    }

    return UserResponse.AvailableEmailDTO.builder().email(encryptedEmail).available(true).build();
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
