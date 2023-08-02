package com.example.admin.user.service;

import com.example.admin.user.dto.SignUpRequest;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.SignUpServiceException;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SignUpRepository signUpRepository;

    @InjectMocks
    private SignUpService signUpService;

    @DisplayName("회원 가입 요청 승인 성공 테스트")
    @Test
    void approve_Success_Test() {
        // Given
        SignUpRequest.ApproveDTO approveDTO = new SignUpRequest.ApproveDTO("user@example.com");
        SignUp signUp = SignUp.builder().email("user@example.com").build();

        Mockito.when(signUpRepository.findByEmail(approveDTO.getEmail()))
                .thenReturn(Optional.of(signUp));

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(new User());

        // When
        // Then
        signUpService.approve(approveDTO);
    }

    @DisplayName("회원 가입 요청 승인 실패 테스트 - 비어있는 DTO")
    @Test
    void approve_Failure_Test_EmptyDTO() {
        // Given
        // When
        // Then
        Assertions.assertThrows(EmptyDtoRequestException.class, () -> {
            signUpService.approve(null);
        });
    }

    @DisplayName("회원 가입 요청 승인 실패 테스트 - 유효하지 않은 Email")
    @Test
    void approve_Failure_Test_InvalidEmail() {
        // Given
        Mockito.when(signUpRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        SignUpRequest.ApproveDTO approveDTO = new SignUpRequest.ApproveDTO("user@example.com");
        // When
        // Then
        Assertions.assertThrows(SignUpServiceException.class, () -> {
            signUpService.approve(approveDTO);
        });
    }
}