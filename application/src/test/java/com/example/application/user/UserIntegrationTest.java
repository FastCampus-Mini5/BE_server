package com.example.application.user;

import static com.example.core.config._security.jwt.JwtTokenProvider.TOKEN_PREFIX;
import static com.example.core.errors.ErrorMessage.NOT_FOUND_USER_TO_RESET_PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.user.service.UserService;
import com.example.core.config._security.SecurityConfig;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception401;
import com.example.core.errors.exception.Exception500;
import com.example.core.errors.exception.UserNotFoundException;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(SecurityConfig.class)
@SpringBootTest
public class UserIntegrationTest {
  private final UserService sut;
  private final SignUpRepository signUpRepository;
  private final PasswordEncoder passwordEncoder;
  private final Encryption encryption;
  @Autowired private UserRepository userRepository;

  public UserIntegrationTest(
      @Autowired UserService sut,
      @Autowired SignUpRepository signUpRepository,
      @Autowired PasswordEncoder passwordEncoder,
      @Autowired Encryption encryption) {
    this.sut = sut;
    this.signUpRepository = signUpRepository;
    this.passwordEncoder = passwordEncoder;
    this.encryption = encryption;
  }

  @DisplayName("[SUCCESS][saveSignUpRequest] 회원 가입 요청 저장 - 성공")
  @Test
  void givenSignUpDTO_whenRequestsSave_thenSavesRequest() {
    // Given
    String testSignUpEmail = "signUpTest@test.com";
    String testUsername = "임시완";
    String testPassword = "signUpTest1234!@";
    Timestamp testHireDate = Timestamp.valueOf(LocalDateTime.of(2023, 8, 8, 20, 20, 0));

    UserRequest.SignUpDTO testSignUpDTO =
        UserRequest.SignUpDTO.builder()
            .email(testSignUpEmail)
            .password(testPassword)
            .username(testUsername)
            .hireDate(testHireDate)
            .build();

    // When
    sut.saveSignUpRequest(testSignUpDTO);
    SignUp testSignUp = signUpRepository.getReferenceByEmail(encryption.encrypt(testSignUpEmail));

    // Then
    assertThat(testSignUp)
        .hasFieldOrProperty("id")
        .hasFieldOrPropertyWithValue("email", encryption.encrypt(testSignUpEmail))
        .hasFieldOrPropertyWithValue("hireDate", testHireDate)
        .hasFieldOrPropertyWithValue("username", encryption.encrypt(testUsername));
    assertTrue(passwordEncoder.matches(testPassword, testSignUp.getPassword()));
  }

  @DisplayName("[FAIL][saveSignUpRequest] 회원 가입 요청 - 이미 가입 요청된 이메일 예외 발생")
  @Test
  void givenAlreadyRequestedEmail_whenRequestsSave_thenFail() {
    // Given
    String testSignUpEmail = "test11@test.com";
    String testUsername = "임시완";
    String testPassword = "signUpTest1234!@";
    Timestamp testHireDate = Timestamp.valueOf(LocalDateTime.of(2023, 8, 8, 20, 20, 0));

    UserRequest.SignUpDTO testSignUpDTO =
        UserRequest.SignUpDTO.builder()
            .email(testSignUpEmail)
            .password(testPassword)
            .username(testUsername)
            .hireDate(testHireDate)
            .build();

    // When
    Throwable t = catchThrowable(() -> sut.saveSignUpRequest(testSignUpDTO));

    // Then
    assertThat(t)
        .isInstanceOf(Exception400.class)
        .hasMessageContaining(ErrorMessage.DUPLICATED_EMAIL);
  }

  @DisplayName("[SUCCESS][signIn] 로그인 요청 - 성공시 JWT 반환")
  @Test
  void givenSignInDTO_whenRequestsSignIn_thenReturnsJWT() {
    // Given
    String loginEmail = "test1@test.com";
    String password = "test";

    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(loginEmail).password(password).build();

    // When
    String jwt = sut.signIn(signInDTO);

    // Then
    assertTrue(jwt.startsWith(TOKEN_PREFIX));
  }

  @DisplayName("[FAIL][signIn] 로그인 요청 - 존재하지 않는 이메일")
  @Test
  void givenNotExistsEmail_whenRequestsSignIn_thenFails() {
    // Given
    String notExistsEmail = "NotExists@test.com";
    String password = "test";

    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(notExistsEmail).password(password).build();

    // When
    Throwable t = catchThrowable(() -> sut.signIn(signInDTO));

    // Then
    assertThat(t)
        .isInstanceOf(Exception401.class)
        .hasMessageContaining(ErrorMessage.USER_NOT_FOUND);
  }

  @DisplayName("[FAIL][signIn] 로그인 요청 - 비밀번호 불일치")
  @Test
  void givenWrongPassword_whenRequestsSignIn_thenFails() {
    // Given
    String loginEmail = "test1@test.com";
    String password = "invalidPassword3123!@!@";

    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(loginEmail).password(password).build();

    // When
    Throwable t = catchThrowable(() -> sut.signIn(signInDTO));

    // Then
    assertThat(t)
        .isInstanceOf(Exception401.class)
        .hasMessageContaining(ErrorMessage.PASSWORD_NOT_MATCH);
  }

  @DisplayName("[SUCCESS][checkEmail] 이메일 중복 체크 - 사용 가능")
  @Test
  void givenEmail_whenCheck_thenReturnsAvailableResponse() {
    // Given
    String availableEmail = "availableEmail@test.com";
    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(availableEmail).build();

    // When
    UserResponse.AvailableEmailDTO availableEmailDTO = sut.checkEmail(checkEmailDTO);

    // Then
    assertThat(availableEmailDTO)
        .hasFieldOrPropertyWithValue("email", availableEmail)
        .hasFieldOrPropertyWithValue("available", true);
  }

  @DisplayName("[SUCCESS][checkEmail] 이메일 중복 체크 - 사용 불가능")
  @Test
  void givenEmail_whenCheck_thenReturnsNotAvailableResponse() {
    // Given
    String notAvailableEmail = "test1@test.com";
    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(notAvailableEmail).build();

    // When
    UserResponse.AvailableEmailDTO availableEmailDTO = sut.checkEmail(checkEmailDTO);

    // Then
    assertThat(availableEmailDTO)
        .hasFieldOrPropertyWithValue("email", notAvailableEmail)
        .hasFieldOrPropertyWithValue("available", false);
  }

  @DisplayName("[SUCCESS][getUserInfo] 회원 상세 정보 조회 - 성공")
  @Test
  void givenUserId_whenRequests_thenReturnsUserInfo() {
    // Given
    Long userId = 1L;

    // When
    UserResponse.UserInfoDTO userInfoDTO = sut.getUserInfoByUserId(userId);

    // Then
    assertThat(userInfoDTO)
        .hasFieldOrPropertyWithValue("email", "test1@test.com")
        .hasFieldOrPropertyWithValue("username", "김영수")
        .hasFieldOrPropertyWithValue("profileImage", "/image/default.png")
        .hasFieldOrPropertyWithValue("remainVacation", 6)
        .hasFieldOrPropertyWithValue("usedVacation", 2);
  }

  @DisplayName("[FAIL][getUserInfo] 회원 상세 정보 조회 - 잘못된 유저 Id")
  @Test
  void givenWrongUserId_whenRequestsUserInfo_thenThrowsException() {
    // Given
    Long userId = -1L;

    // When
    Throwable t = catchThrowable(() -> sut.getUserInfoByUserId(userId));

    // Then
    assertThat(t)
        .isInstanceOf(Exception400.class)
        .hasMessageContaining(ErrorMessage.USER_NOT_FOUND);
  }

  @DisplayName("[SUCCESS][updateUserInfo] 회원 상세 정보 업데이트 - 성공")
  @Test
  void givenUserIdAndUpdateInfoDTO_whenRequests_thenUpdatesUserInfo() {
    // Given
    Long userId = 3L;
    String updatedProfileImg = "updatedImg";
    String updatedPassword = "updatedPassword!@3";
    UserRequest.UpdateInfoDTO updateInfoDTO =
        UserRequest.UpdateInfoDTO.builder()
            .profileImage(updatedProfileImg)
            .password(updatedPassword)
            .build();

    // When
    sut.updateUserInfoByUserId(userId, updateInfoDTO);
    User user = userRepository.findById(userId).orElseThrow();

    // Then
    assertTrue(passwordEncoder.matches(updatedPassword, user.getPassword()));
    assertThat(user).hasFieldOrPropertyWithValue("profileImage", updatedProfileImg);
  }

  @DisplayName("[FAIL][updateUserInfo] 회원 상세 정보 업데이트 - 잘못된 유저 ID")
  @Test
  void givenWrongUserId_whenRequestsUpdate_thenThrowsException() {
    // Given
    Long userId = -1L;
    String updatedProfileImg = "updatedImg";
    String updatedPassword = "updatedPassword!@3";
    UserRequest.UpdateInfoDTO updateInfoDTO =
        UserRequest.UpdateInfoDTO.builder()
            .profileImage(updatedProfileImg)
            .password(updatedPassword)
            .build();

    // When
    Throwable t = catchThrowable(() -> sut.updateUserInfoByUserId(userId, updateInfoDTO));

    // Then
    assertThat(t)
        .isInstanceOf(Exception500.class)
        .hasMessageContaining(ErrorMessage.USER_NOT_FOUND);
  }

  @DisplayName("[SUCCESS][passwordReset] 비밀번호 초기화 요청 - 성공")
  @Test
  void givenResetPasswordDTO_whenRequests_thenResetPassword() {
    // Given
    String targetEmail = "test1@test.com";
    UserRequest.ResetPasswordDTO resetPasswordDTO =
        UserRequest.ResetPasswordDTO.builder().email(targetEmail).build();

    // When
    String tempPassword = sut.resetPassword(resetPasswordDTO);

    // Then
    assertEquals(tempPassword.length(), 6);
  }

  @DisplayName("[FAIL][passwordReset] 비밀번호 초기화 요청 - 존재하지 않는 이메일")
  @Test
  void givenWrongEmail_whenRequests_thenThrowsNotFoundUserToResetPassword() {
    // Given
    String targetEmail = "invalidEmail@test.com";
    UserRequest.ResetPasswordDTO resetPasswordDTO =
        UserRequest.ResetPasswordDTO.builder().email(targetEmail).build();

    // When
    Throwable t = catchThrowable(() -> sut.resetPassword(resetPasswordDTO));

    // Then
    assertThat(t)
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(NOT_FOUND_USER_TO_RESET_PASSWORD);
  }
}
