package com.example.application.user.service;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.*;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final SignUpRepository signUpRepository;
  private final UserRepository userRepository;
  private final VacationInfoRepository vacationInfoRepository;
  private final Encryption encryption;

  @Transactional
  public void saveSignUpRequest(UserRequest.SignUpDTO signUpDTO) {
    if (signUpRepository.existsByEmail(encryption.encrypt(signUpDTO.getEmail())))
      throw new Exception400(ErrorMessage.DUPLICATED_EMAIL);

    signUpRepository.save(signUpDTO.toEncryptedEntity(passwordEncoder, encryption));
  }

  @Transactional(readOnly = true)
  public String signIn(UserRequest.SignInDTO signInDTO) {
    if (signInDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNIN);

    User user =
        userRepository
            .findByEmail(encryption.encrypt(signInDTO.getEmail()))
            .orElseThrow(
                () -> {
                  throw new Exception401(ErrorMessage.USER_NOT_FOUND);
                });

    if (!passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
      throw new Exception401(ErrorMessage.PASSWORD_NOT_MATCH);
    }

    return JwtTokenProvider.create(user);
  }

  @Transactional(readOnly = true)
  public UserResponse.AvailableEmailDTO checkEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
    if (checkEmailDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_CHECK_EMAIL);

    String plainEmail = checkEmailDTO.getEmail();
    String encryptedEmail = encryption.encrypt(checkEmailDTO.getEmail());

    if (userRepository.existsByEmail(encryptedEmail)) {
      return UserResponse.AvailableEmailDTO.builder().email(plainEmail).available(false).build();
    }

    return UserResponse.AvailableEmailDTO.builder().email(plainEmail).available(true).build();
  }

  @Transactional(readOnly = true)
  public UserResponse.UserInfoDTO getUserInfoByUserId(Long userId) {

    User user = userRepository.getReferenceById(userId);
    VacationInfo vacationInfo = vacationInfoRepository.getReferenceByUserId(userId);

    return UserResponse.UserInfoDTO.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .profileImage(user.getProfileImage())
        .hireDate(user.getHireDate())
        .remainVacation(vacationInfo.getRemainVacation())
        .usedVacation(vacationInfo.getUsedVacation())
        .build()
        .toDecryptedDTO(encryption);
  }

  @Transactional
  public void updateUserInfoByUserId(Long userId, UserRequest.UpdateInfoDTO updateInfoDTO) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> {
                  throw new Exception500(ErrorMessage.USER_NOT_FOUND);
                });

    String encryptedPassword = passwordEncoder.encode(updateInfoDTO.getPassword());

    user.setProfileImage(updateInfoDTO.getProfileImage());
    user.setPassword(encryptedPassword);
  }

  @Transactional
  public String resetPassword(UserRequest.ResetPasswordDTO resetPasswordDTO) {
    if (resetPasswordDTO == null)
      throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_FIND_PASSWORD);

    final String email = resetPasswordDTO.getEmail();
    String encryptedEmail = encryption.encrypt(email);

    User user =
        userRepository
            .findByEmail(encryptedEmail)
            .orElseThrow(
                () -> new UserNotFoundException(ErrorMessage.NOT_FOUND_USER_TO_RESET_PASSWORD));

    String tempPassword = getRandomSixString();
    String encodedPassword = passwordEncoder.encode(tempPassword);
    user.setPassword(encodedPassword);

    return tempPassword;
  }

  private String getRandomSixString() {
    UUID uuid = UUID.randomUUID();
    String uuidStr = uuid.toString().replace("-", "");
    return uuidStr.substring(0, 6);
  }
}
