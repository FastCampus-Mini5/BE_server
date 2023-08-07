package com.example.admin.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.admin.user.dto.UserRequest;
import com.example.admin.user.dto.UserResponse;
import com.example.admin.user.service.UserService;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @DisplayName("전체 유저 정보 조회 통합 테스트 - 성공")
    @Test
    void getAllList_Success_Test() {
        // Given
        Pageable pageable = PageRequest.of(1, 4);

        // When
        Page<UserResponse.ListDTO> actual = userService.getAllUsers(pageable);

        // Then
        Assertions.assertEquals(4, actual.getSize());
        Assertions.assertEquals("senior", actual.getContent().get(0).getUsername());
        Assertions.assertEquals("junior", actual.getContent().get(1).getUsername());
    }

    @DisplayName("전체 유저 정보 조회 통합 테스트 - 실패 [빈 페이징 데이터 전달]")
    @Test
    void getAllList_Failure_Test_GivenEmptyPageableData() {
        // Given
        Pageable pageable = null;

        // When
        // Then
        Assertions.assertThrows(EmptyPagingDataRequestException.class, () ->
                userService.getAllUsers(pageable));
    }

    @DisplayName("관리자 로그인 테스트 - 성공 [토큰 생성 및 검증]")
    @Test
    void signIn_Success_Test() {
        // Given
        UserRequest.SignInDTO signInDTO =
                new UserRequest.SignInDTO("admin", "admin");

        // When
        UserResponse.SignInDTO response = userService.signIn(signInDTO);
        String token = response.getJwt();
        String jwt = token.replace(JwtTokenProvider.TOKEN_PREFIX, "");
        DecodedJWT decodedJWT = JwtTokenProvider.verify(jwt);

        // Then
        Assertions.assertEquals("ADMIN", decodedJWT.getClaim("role").asString());
    }

    @DisplayName("관리자 로그인 테스트 - 실패 [비어있는 DTO 전달]")
    @Test
    void signIn_Failure_Test_GivenEmptyDTO() {
        // Given
        UserRequest.SignInDTO signInDTO = null;

        // When
        // Then
        Assertions.assertThrows(EmptyDtoRequestException.class, () ->
                userService.signIn(signInDTO));
    }

    @DisplayName("관리자 로그인 테스트 - 실패 [유효하지 않은 관리자 정보 전달]")
    @Test
    void signIn_Failure_Test_GivenInvalidSignInData() {
        // Given
        UserRequest.SignInDTO signInDTO =
                new UserRequest.SignInDTO("admin", "invalidPassword");
        // When
        // Then
        Assertions.assertThrows(BadCredentialsException.class, () ->
                userService.signIn(signInDTO));
    }
}
