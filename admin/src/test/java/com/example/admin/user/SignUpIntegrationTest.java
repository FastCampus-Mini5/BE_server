package com.example.admin.user;

import com.example.admin.user.dto.SignUpRequest;
import com.example.admin.user.dto.SignUpResponse;
import com.example.admin.user.service.SignUpService;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
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

    @DisplayName("회원 가입 요청 리스트 조회 테스트 - 성공")
    @Test
    void getAllList_Success_Test() {
        // Given
        Pageable pageable = PageRequest.of(1, 4);

        // When
        Page<SignUpResponse.ListDTO> actual = signUpService.getAllList(pageable);

        // Then
        Assertions.assertEquals(4, actual.getSize());
        Assertions.assertEquals(6, actual.getTotalElements());
        Assertions.assertEquals("senior", actual.getContent().get(0).getUsername());
        Assertions.assertEquals("junior", actual.getContent().get(1).getUsername());
    }

    @DisplayName("회원 가입 요청 리스트 조회 테스트 - 실패 [빈 페이징 데이터 전달]")
    @Test
    void getAllList_Failure_Test_GivenEmptyPageableData() {
        // Given
        Pageable pageable = null;

        // When
        // Then
        Assertions.assertThrows(EmptyPagingDataRequestException.class, () ->
                signUpService.getAllList(pageable));
    }
}
