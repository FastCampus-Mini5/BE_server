package com.example.application.user.service;

import static com.example.core.errors.ErrorMessage.PASSWORD_NOT_MATCH;
import static com.example.core.errors.ErrorMessage.USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.*;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.util.Optional;
import javax.persistence.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("[UserServiceClass] 서비스 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks private UserService sut;
  @Mock private UserRepository userRepository;
  @Mock private VacationInfoRepository vacationInfoRepository;
  @Mock private SignUpRepository signUpRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private Encryption encryption;

  UserServiceTest() {}

  @DisplayName("[SUCCESS][saveSignUpRequest] 회원 가입 요청이 들어오면 해당 요청에 있는 정보들을 저장한다.")
  @Test
  void givenSignUpDTO_whenSaveSignUpRequest_thenSavesSignUpInfo() {
    // Given
    UserRequest.SignUpDTO signUpDTO =
        UserRequest.SignUpDTO.builder()
            .username("testusername")
            .password("testPassword!@")
            .email("testemail@test.com")
            .hireDate(Timestamp.valueOf("2023-08-01 00:00:00"))
            .build();

    ArgumentCaptor<SignUp> signUpCaptor = ArgumentCaptor.forClass(SignUp.class);

    // When
    sut.saveSignUpRequest(signUpDTO);

    // Then
    then(signUpRepository).should().save(signUpCaptor.capture());
    SignUp capturedSignUp = signUpCaptor.getValue();
    assertEquals(encryption.encrypt(signUpDTO.getEmail()), capturedSignUp.getEmail());
  }

  @DisplayName("[FAIL][signIn] 로그인 이메일 불일치 - 실패")
  @Test
  void givenWrongEmail_whenSignIn_thenSignInFailed() {
    // Given
    String wrongEmail = "wrongEmail@test.com";
    String encryptedEmail = "encrypted";
    String password = "password!231!";
    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(wrongEmail).password(password).build();

    // When
    when(encryption.encrypt(signInDTO.getEmail())).thenReturn(encryptedEmail);
    when(userRepository.findByEmail(encryptedEmail)).thenThrow(new Exception401(USER_NOT_FOUND));
    Throwable t = catchThrowable(() -> sut.signIn(signInDTO));

    // Then
    assertThat(t).isInstanceOf(Exception401.class).hasMessageContaining(USER_NOT_FOUND);
  }

  @DisplayName("[FAIL][signIn] 로그인 비밀번호 불일치 - 실패")
  @Test
  void givenWrongPassword_whenSignIn_thenSignInFailed() {
    // Given
    String wrongEmail = "wrongEmail@test.com";
    String encryptedEmail = "encrypted";
    String password = "password!231!";
    String encodedPassword = "encoded";

    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(wrongEmail).password(password).build();
    User user = User.builder().email(encryptedEmail).password(encodedPassword).build();
    Optional<User> userOptional = Optional.of(user);

    // When
    when(encryption.encrypt(signInDTO.getEmail())).thenReturn(encryptedEmail);
    when(userRepository.findByEmail(encryptedEmail)).thenReturn(userOptional);
    when(passwordEncoder.matches(signInDTO.getPassword(), user.getPassword()))
        .thenReturn(Boolean.FALSE);
    Throwable t = catchThrowable(() -> sut.signIn(signInDTO));

    // Then
    assertThat(t).isInstanceOf(Exception401.class).hasMessageContaining(PASSWORD_NOT_MATCH);
  }

  @DisplayName("[SUCCESS][checkEmail] 이메일 중복체크 - 사용가능한 이메일")
  @Test
  void givenEmail_whenCheckEmail_thenReturnsAvailable() {
    // Given
    String email = "test@email.com";
    String encryptedEmail = "encrypted_test@email.com";

    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(email).build();

    given(encryption.encrypt(email)).willReturn(encryptedEmail);

    given(userRepository.existsByEmail(encryptedEmail)).willReturn(Boolean.FALSE);

    // When
    UserResponse.AvailableEmailDTO actual = sut.checkEmail(checkEmailDTO);

    // Then
    then(userRepository).should().existsByEmail(encryptedEmail);
    assertThat(actual)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("available", Boolean.TRUE);
  }

  @DisplayName("[FAIL][checkEmail] 이메일 중복체크 - 중복 이메일")
  @Test
  void givenDuplicatedEmail_whenCheckEmail_thenReturnsNotAvailable() {
    // Given
    String email = "test@email.com";
    String encryptedEmail = "encrypted_test@email.com";

    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(email).build();

    given(encryption.encrypt(email)).willReturn(encryptedEmail);

    given(userRepository.existsByEmail(encryptedEmail)).willReturn(Boolean.TRUE);

    // When
    UserResponse.AvailableEmailDTO actual = sut.checkEmail(checkEmailDTO);

    // Then
    then(userRepository).should().existsByEmail(encryptedEmail);
    assertThat(actual)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("available", Boolean.FALSE);
  }

  @DisplayName("[FAIL][checkEmail] null DTO  - 예외 발생")
  @Test
  void givenNothing_whenCheckEmail_thenThrowsException() {
    // Given

    // When
    Throwable t = Assertions.catchThrowable(() -> sut.checkEmail(null));

    // Then
    assertThat(t)
        .isInstanceOf(Exception500.class)
        .hasMessageContaining(ErrorMessage.EMPTY_DATA_TO_CHECK_EMAIL);
  }

  @DisplayName("[SUCCESS][getUserInfoByUserId] 유저 ID가 주어지면 해당 유저 정보를 불러온다.")
  @Test
  void givenUserId_whenGetUserInfoByUserId_thenReturnsUserInfoDTO() {
    // Given
    Long userId = 1L;
    String rawEmail = "test@test.com";
    String rawUsername = "testUsername";
    String encodedUsername = "encodedTestuser";
    String encodedEmail = "encodedEmail";
    String profileImage = "base64Image";
    Timestamp hireDate = Timestamp.valueOf("2023-06-01 00:00:00");
    given(encryption.decrypt(encodedUsername)).willReturn(rawUsername);
    given(encryption.decrypt(encodedEmail)).willReturn(rawEmail);

    User user =
        User.builder()
            .id(userId)
            .username(encodedUsername)
            .email(encodedEmail)
            .profileImage(profileImage)
            .hireDate(hireDate)
            .build();

    VacationInfo vacationInfo =
        VacationInfo.builder().user(user).remainVacation(3).usedVacation(12).build();

    // When
    when(userRepository.getReferenceById(userId)).thenReturn(user);
    when(vacationInfoRepository.getReferenceByUserId(userId)).thenReturn(vacationInfo);
    UserResponse.UserInfoDTO actual = sut.getUserInfoByUserId(userId);

    // Then
    then(userRepository).should().getReferenceById(userId);
    then(vacationInfoRepository).should().getReferenceByUserId(userId);
    assertThat(actual)
        .hasFieldOrPropertyWithValue("username", rawUsername)
        .hasFieldOrPropertyWithValue("email", rawEmail)
        .hasFieldOrPropertyWithValue("profileImage", profileImage)
        .hasFieldOrPropertyWithValue("hireDate", hireDate);
  }

  @DisplayName("[FAIL][getUserInfoByUserId] 존재하지 않는 유저 ID - 에러 발생")
  @Test
  void givenInvalidUserId_whenGetUserInfoByUserId_thenThrowsError() {
    // Given
    Long userId = -1L;

    // When & Then
    when(userRepository.getReferenceById(userId)).thenThrow(new EntityNotFoundException());
    Throwable t = Assertions.catchThrowable(() -> sut.getUserInfoByUserId(userId));

    // Then
    then(userRepository).should().getReferenceById(userId);
    assertThat(t).isInstanceOf(Exception400.class).hasMessageContaining(USER_NOT_FOUND);
  }

  @DisplayName("[SUCCESS][updateUserInfoByUserId] 유저 ID와 업데이트 정보를 전달하면 유저 정보를 업데이트 한다.")
  @Test
  void givenUserIdAndUpdateInfoDTO_whenUpdateUserInfoByUserId_thenSavesUserInfo() {
    // Given
    Long userId = 1L;

    String profileImage = "updatedImg.png";
    String rawPassword = "updatedPassword!@";
    String encryptedPassword = "encryptedPassword123!@";

    UserRequest.UpdateInfoDTO updateInfoDTO =
        UserRequest.UpdateInfoDTO.builder()
            .profileImage(profileImage)
            .password(rawPassword)
            .build();

    Timestamp hireDate = Timestamp.valueOf("2023-06-01 00:00:00");

    User user =
        User.builder()
            .id(userId)
            .username("testuser")
            .email("testuser@test.com")
            .profileImage("default.img")
            .hireDate(hireDate)
            .build();

    Optional<User> optionalUser = Optional.of(user);

    // When
    when(passwordEncoder.encode(updateInfoDTO.getPassword())).thenReturn(encryptedPassword);
    when(userRepository.findById(userId)).thenReturn(optionalUser);
    sut.updateUserInfoByUserId(userId, updateInfoDTO);

    // Then
    then(passwordEncoder).should().encode(updateInfoDTO.getPassword());
    assertThat(user)
        .hasFieldOrPropertyWithValue("profileImage", profileImage)
        .hasFieldOrPropertyWithValue("password", encryptedPassword);
  }

  @DisplayName("[FAIL][updateUserInfoByUserId] 존재하지 않는 유저 ID를 전달하는 경우 예외 발생")
  @Test
  void givenNotExistUserId_whenUpdateUserInfoByUserId_thenThrowsException() {
    // Given
    Long userId = 1L;
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // When
    Throwable t =
        Assertions.catchThrowable(
            () -> sut.updateUserInfoByUserId(userId, any(UserRequest.UpdateInfoDTO.class)));

    // Then
    then(userRepository).should().findById(userId);
    assertThat(t).isInstanceOf(Exception500.class);
  }

  @DisplayName("[SUCCESS][findPassword] 유저 이메일을 전달받고 임시비밀번호를 이메일로 발급한다.")
  @Test
  void givenFindPasswordDTO_whenFindPasswordRequest_thenUpdateTempPassword() {
    // Given
    UserRequest.ResetPasswordDTO resetPasswordDTO =
        new UserRequest.ResetPasswordDTO("user@example.com");

    User user = User.builder().email("encrypted_user@excmple.com").password("1234").build();

    when(encryption.encrypt(ArgumentMatchers.anyString()))
        .thenAnswer(
            invocation -> {
              String rawData = invocation.getArgument(0);
              return "encrypted_" + rawData;
            });

    when(userRepository.findByEmail(ArgumentMatchers.anyString()))
        .thenReturn(Optional.of(user)); // When
    // When
    sut.resetPassword(resetPasswordDTO);

    // Then
    org.junit.jupiter.api.Assertions.assertNotEquals("1234", user.getPassword());
  }

  @DisplayName("[FAIL][findPassword] 빈 DTO를 전달할 경우 오류 발생")
  @Test
  void givenEmptyFindPasswordDTO_whenFindPasswordRequest_thenThrowsException() {
    // Given
    UserRequest.ResetPasswordDTO resetPasswordDTO = null;

    // When
    // Then
    org.junit.jupiter.api.Assertions.assertThrows(
        EmptyDtoRequestException.class, () -> sut.resetPassword(resetPasswordDTO));
  }

  @DisplayName("[FAIL][findPassword] 유효하지 않은 이메일을 전달할 경우 오류 발생")
  @Test
  void givenInvalidFindPasswordDTO_whenFindPasswordRequest_thenThrowsException() {
    // Given
    UserRequest.ResetPasswordDTO findPasswordDTO =
        new UserRequest.ResetPasswordDTO("user@example.com");

    when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

    when(encryption.encrypt(ArgumentMatchers.anyString()))
        .thenAnswer(
            invocation -> {
              String rawData = invocation.getArgument(0);
              return "encrypted_" + rawData;
            });
    // When
    // Then
    org.junit.jupiter.api.Assertions.assertThrows(
        UserNotFoundException.class, () -> sut.resetPassword(findPasswordDTO));
  }
}
