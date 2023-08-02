package com.example.admin.user.service;

import com.example.admin.user.dto.UserResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private VacationInfoRepository vacationInfoRepository;

    @Mock
    private Encryption encryption;

    @InjectMocks
    private UserService userService;

    @DisplayName("전체 유저 조회 성공 테스트")
    @Test
    void getAllList_Success_Test() {
        // Given
        List<VacationInfo> mockList = List.of(
                VacationInfo.builder().id(1L).remainVacation(3)
                        .user(new User()).build(),

                VacationInfo.builder().id(2L).remainVacation(4)
                        .user(new User()).build(),

                VacationInfo.builder().id(3L).remainVacation(5)
                        .user(new User()).build()
        );

        Pageable pageable = PageRequest.of(0, 3);
        Page<VacationInfo> mockPage = new PageImpl<>(mockList, pageable, 1);

        Mockito.when(vacationInfoRepository.findAll(pageable))
                .thenReturn(mockPage);

        // When
        Page<UserResponse.ListDTO> actual = userService.getAllUsers(pageable);

        // Then
        Assertions.assertEquals(3, actual.getTotalElements());
        Assertions.assertEquals(1, actual.getTotalPages());
    }

    @DisplayName("전체 유저 조회 실패 테스트 - 빈 페이지 정보 요청")
    @Test
    void getAllList_failure_Test() {
        // Given
        // When
        // Then
        Assertions.assertThrows(EmptyPagingDataRequestException.class, () -> {
            userService.getAllUsers(null);
        });
    }
}