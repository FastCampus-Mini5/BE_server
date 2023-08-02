package com.example.application.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception500;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("[UserServiceClass] 서비스 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @InjectMocks private UserService sut;
  @Mock private UserRepository userRepository;
  @Mock private SignUpRepository signUpRepository;
  @Mock private PasswordEncoder passwordEncoder;

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
    assertEquals(signUpDTO.getEmail(), capturedSignUp.getEmail());
  }

  @DisplayName("[FAIL][saveSignUpRequest] 회원 가입 요청이 전달되지 않은 경우 예외 발생")
  @Test
  void givenNothing_whenSaveSignUpRequest_thenThrowsException() {
    // Given

    // When
    Throwable t = catchThrowable(() -> sut.saveSignUpRequest(null));

    // Then
    then(signUpRepository).shouldHaveNoInteractions();
    assertThat(t)
        .isInstanceOf(Exception500.class)
        .hasMessageContaining(ErrorMessage.EMPTY_DATA_TO_SIGNUP);
  }

  // TODO : signIn 메서드 리팩토링 후 작성 시작
  @DisplayName("[SUCCESS][signIn] 로그인 - 성공")
  @Test
  void givenSignInDTO_whenSignIn_thenReturnsJWT() {
    // Given
    String email = "test@email.com";
    String rawPassword = "rawPassword!@";
    String encodedPassword = "encodedPassword!@";
    UserRequest.SignInDTO signInDTO =
        UserRequest.SignInDTO.builder().email(email).password(rawPassword).build();

    User user = User.builder().email(email).password(encodedPassword).build();

    Optional<User> userOptional = Optional.of(user);

    given(userRepository.findByEmail(email)).willReturn(userOptional);
    given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(Boolean.TRUE);

    // When
    String actualJWT = sut.signIn(signInDTO);

    // Then
    assertThat(actualJWT).startsWith("Bearer ");
  }

  // TODO : signIn 메서드 리팩토링 후 작성 시작
  @DisplayName("[FAIL][signIn] 잘못된 로그인 시도 - 실패")
  @Test
  void givenWrongSignInInfo_whenSignIn_thenReturnsJWT() {}

  @DisplayName("[SUCCESS][checkEmail] 이메일 중복체크 - 사용가능한 이메일")
  @Test
  void givenEmail_whenCheckEmail_thenReturnsAvailable() {
    // Given
    String email = "testemail.com";
    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(email).build();

    given(userRepository.existsByEmail(email)).willReturn(Boolean.FALSE);

    // When
    UserResponse.AvailableEmailDTO actual = sut.checkEmail(checkEmailDTO);

    // Then
    then(userRepository).should().existsByEmail(email);
    assertThat(actual)
        .hasFieldOrPropertyWithValue("email", email)
        .hasFieldOrPropertyWithValue("available", Boolean.TRUE);
  }

  @DisplayName("[FAIL][checkEmail] 이메일 중복체크 - 중복 이메일")
  @Test
  void givenDuplicatedEmail_whenCheckEmail_thenReturnsNotAvailable() {
    // Given
    String email = "test@email.com";
    UserRequest.CheckEmailDTO checkEmailDTO =
        UserRequest.CheckEmailDTO.builder().email(email).build();

    given(userRepository.existsByEmail(email)).willReturn(Boolean.TRUE);

    // When
    UserResponse.AvailableEmailDTO actual = sut.checkEmail(checkEmailDTO);

    // Then
    then(userRepository).should().existsByEmail(email);
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
}
