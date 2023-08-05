package com.example.admin.user;

import com.example.admin.user.dto.SignUpRequest;
import com.example.admin.user.service.SignUpService;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.SignUpServiceException;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class SignUpIntegrationTest {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignUpRepository signUpRepository;

    @Autowired
    private Encryption encryption;

    @DisplayName("회원 가입 요청 승인 테스트 - 성공")
    @Test
    void approve_Success_Test() {
        // Given
        final String email = "test7@test.com";
        final String encryptedEmail = encryption.encrypt(email);

        SignUpRequest.ApproveDTO approveDTO =
                new SignUpRequest.ApproveDTO(email);

        // When
        signUpService.approve(approveDTO);
        Optional<SignUp> signUpOptional = signUpRepository.findByEmail(encryptedEmail);
        Optional<User> userOptional = userRepository.findByEmail(encryptedEmail);

        // Then
        Assertions.assertTrue(signUpOptional.isEmpty());
        Assertions.assertTrue(userOptional.isPresent());
    }

    @DisplayName("회원 가입 요청 승인 테스트 - 실패 [유효하지 않은 Email 요청]")
    @Test
    void approve_Failure_Test_GivenInvalidEmail() {
        // Given
        SignUpRequest.ApproveDTO approveDTO =
                new SignUpRequest.ApproveDTO("invalid@email.com");

        // When
        // Then
        Assertions.assertThrows(SignUpServiceException.class, () ->
                signUpService.approve(approveDTO));

        try {
            signUpService.approve(approveDTO);
        } catch (SignUpServiceException exception) {
            Assertions.assertEquals(ErrorMessage.NOT_FOUND_SIGNUP, exception.getMessage());
        }
    }

    @DisplayName("회원 가입 요청 승인 테스트 - 실패 [비어있는 DTO 요청]")
    @Test
    void approve_Failure_Test_GivenEmptyDTO() {
        // Given
        SignUpRequest.ApproveDTO approveDTO = null;

        // When
        // Then
        Assertions.assertThrows(EmptyDtoRequestException.class, () ->
                signUpService.approve(approveDTO));
    }
}
