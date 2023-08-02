package com.example.admin.schedule.duty.service;

import com.example.admin.schedule.duty.dto.DutyRequest;
import com.example.admin.schedule.duty.dto.DutyResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.DutyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DutyServiceTest {

    @Mock
    private DutyRepository dutyRepository;

    @Mock
    private Encryption encryption;

    @InjectMocks
    private DutyService dutyService;

    @DisplayName("당직 신청 승인 성공")
    @Test
    void testDutyApproveSuccess() {
        //given
        Long dutyId = 1L;
        DutyRequest.StatusDTO statusDTO = new DutyRequest.StatusDTO(dutyId, "APPROVE");

        Duty existDuty = createDuty(dutyId, createUser(1L, "88kd02Gzscm5encDICfxJA==", "kjoJiIPBz6f5YQXgKxpurQ=="), "2023-07-01 00:00:00");
        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(existDuty));

        //when
        dutyService.updateByStatus(statusDTO);

        //then
        verify(dutyRepository, times(1)).findById(dutyId);
        verify(dutyRepository, times(1)).save(existDuty);

        assertEquals(Status.APPROVE, existDuty.getStatus());
    }

    @DisplayName("당직 신청 승인 실패 - 유효하지 않은 상태")
    @Test
    void testDutyApproveFailWithStatus() {
        //given
        Long dutyId = 1L;
        DutyRequest.StatusDTO statusDTO = new DutyRequest.StatusDTO(dutyId, "INVALID_STATUS");

        Duty existingDuty = createDuty(dutyId, createUser(1L, "Eg4iD3nDL3l+g28tUHHnIg==", "VynYMl7zNOhDiqMVFC6ODg=="), "2023-07-01 00:00:00");
        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(existingDuty));

        //when,then
        assertThrows(ValidStatusException.class, () -> dutyService.updateByStatus(statusDTO));

        verify(dutyRepository, times(1)).findById(dutyId);
        verify(dutyRepository, never()).save(any());
    }

    @DisplayName("당직 신청 승인 실패 - 해당 당직 데이터가 없는 경우")
    @Test
    void testDutyApproveFailWithDTO() {
        //given
        Long dutyId = 1L;
        DutyRequest.StatusDTO statusDTO = new DutyRequest.StatusDTO(dutyId, "APPROVE");

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(ScheduleServiceException.class, () -> dutyService.updateByStatus(statusDTO));

        verify(dutyRepository, times(1)).findById(dutyId);
        verify(dutyRepository, never()).save(any());
    }

    private User createUser(Long id, String username, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .username(username)
                .password("password123")
                .role("user")
                .profileImage("profile" + id + ".jpg")
                .hireDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private Duty createDuty(Long id, User user, String dutyDate) {
        return Duty.builder()
                .id(id)
                .user(user)
                .dutyDate(Timestamp.valueOf(dutyDate))
                .status(Status.PENDING)
                .approvalDate(null)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .updatedDate(null)
                .build();
    }

    @DisplayName("당직 목록 조회 성공")
    @Test
    void testGetDutyListSuccess() {
        // given
        String validStatus = "PENDING";
        Pageable pageable = mock(Pageable.class);

        List<User> testUsers = List.of(
                createUser(1L, "88kd02Gzscm5encDICfxJA==", "kjoJiIPBz6f5YQXgKxpurQ=="),
                createUser(2L, "Eg4iD3nDL3l+g28tUHHnIg==", "VynYMl7zNOhDiqMVFC6ODg=="),
                createUser(3L, "5A/8pW1DNtMaZnmlWiBcag==", "9ef2PAWiR6rG9yeTqXyflg==")
        );

        List<Duty> dutyList = List.of(
                createDuty(1L, testUsers.get(0), "2023-07-01 00:00:00"),
                createDuty(2L, testUsers.get(1), "2023-07-03 00:00:00"),
                createDuty(3L, testUsers.get(2), "2023-07-05 00:00:00")
        );

        Page<Duty> mockPage = new PageImpl<>(dutyList, pageable, dutyList.size());
        when(dutyRepository.findDutyByStatus(pageable, Status.PENDING)).thenReturn(mockPage);

        //when
        Page<DutyResponse.ListDTO> result = dutyService.dutyListByStatus(pageable, validStatus);

        //then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(dutyRepository, times(1)).findDutyByStatus(pageable, Status.PENDING);
    }

    @DisplayName("당직 목록 조회 실패 - 유효하지 않은 상태")
    @Test
    void testGetDutyListFailWithStatus() {
        //given
        String invalidStatus = "INVALID_STATUS";
        Pageable pageable = mock(Pageable.class);

        //when,then
        assertThrows(ValidStatusException.class,
                () -> dutyService.dutyListByStatus(pageable, invalidStatus));
        verify(dutyRepository, never()).findDutyByStatus(any(), any());
    }

    @DisplayName("당직 목록 조회 실패 - 빈 페이지")
    @Test
    void testGetDutyListFailWithPage() {
        //given
        String validStatus = "APPROVED";
        Pageable pageable = null;

        //when,then
        assertThrows(EmptyPagingDataRequestException.class,
                () -> dutyService.dutyListByStatus(pageable, validStatus));
        verify(dutyRepository, never()).findDutyByStatus(any(), any());
    }
}
