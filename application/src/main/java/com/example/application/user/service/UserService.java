package com.example.application.user.service;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.utils.TempPasswordMailSender;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.Exception500;
import com.example.core.errors.exception.MailSendFailureException;
import com.example.core.errors.exception.UserNotFoundException;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final SignUpRepository signUpRepository;
  private final UserRepository userRepository;
  private final VacationInfoRepository vacationInfoRepository;
  private final Encryption encryption;
  private final TempPasswordMailSender mailSender;

  public void saveSignUpRequest(UserRequest.SignUpDTO signUpDTO) {
    if (signUpDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNUP);

    signUpRepository.save(signUpDTO.toEntityEncrypted(passwordEncoder, encryption));
  }

  public String signIn(UserRequest.SignInDTO signInDTO) {
    if (signInDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_SIGNIN);

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(
            encryption.encrypt(signInDTO.getEmail()), signInDTO.getPassword());

    Authentication authentication = authenticationManager.authenticate(token);

    PrincipalUserDetail userDetail = (PrincipalUserDetail) authentication.getPrincipal();

    final User user = userDetail.getUser();

    return JwtTokenProvider.create(user);
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

  @Transactional
  public void passwordReset(UserRequest.FindPasswordDTO findPasswordDTO) {
    if (findPasswordDTO == null)
      throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_FIND_PASSWORD);

    final String email = findPasswordDTO.getEmail();
    String encryptedEmail = encryption.encrypt(email);

    User user = userRepository.findByEmail(encryptedEmail)
            .orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_USER_TO_RESET_PASSWORD));

    String tempPassword = getRandomSixString();
    String encodedPassword = passwordEncoder.encode(tempPassword);
    user.setPassword(encodedPassword);

    try{
      mailSender.send(email, tempPassword);
    } catch (Exception exception) {
      throw new MailSendFailureException();
    }
  }

  private String getRandomSixString() {
    UUID uuid = UUID.randomUUID();
    String uuidStr = uuid.toString().replace("-", "");
    return uuidStr.substring(0, 6);
  }
}
