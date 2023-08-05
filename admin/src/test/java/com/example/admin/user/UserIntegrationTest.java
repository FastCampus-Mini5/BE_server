package com.example.admin.user;

import com.example.admin.user.dto.UserResponse;
import com.example.admin.user.service.UserService;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
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
}
