package com.example.admin.schedule.duty.service;

import com.example.admin.schedule.duty.dto.DutyRequest;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DutyServiceTest {

    @Mock
    private DutyRepository dutyRepository;

    @InjectMocks
    private DutyService dutyService;

    @DisplayName("당직 신청 승인 성공")
    @Test
    void testDutyApproveSuccess() {
        //given
        Long dutyId = 1L;
        DutyRequest.StatusDTO statusDTO = new DutyRequest.StatusDTO(dutyId, "APPROVE");

        Duty existDuty = createDuty(dutyId, createUser(1L, "user1"), "2023-07-01 00:00:00");
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

        Duty existingDuty = createDuty(dutyId, createUser(1L, "user1"), "2023-07-01 00:00:00");
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

    private User createUser(Long id, String username) {
        return User.builder()
                .id(id)
                .email("test" + id + "@email.com")
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
}
