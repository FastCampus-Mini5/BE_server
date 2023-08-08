package com.example.application.schedule.duty.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.application.schedule.duty.dto.DutyRequest;
import com.example.application.schedule.duty.dto.DutyResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception403;
import com.example.core.errors.exception.Exception404;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.DutyRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DutyServiceTest {

    @Mock
    private DutyRepository dutyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DutyService dutyService;

    @Mock
    private Encryption encryption;

    @DisplayName("유저 당직 신청 성공")
    @Test
    void requestDuty_Success() {
        // given
        Long userId = 1L;
        DutyRequest.AddDTO dutyRequest = createDutyRequest("2023-07-01 00:00:00");

        User user = createUser(userId, "user1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Duty duty = createDuty(1L, user, "2023-07-01 00:00:00");
        when(dutyRepository.findByUserAndDutyDate(user, duty.getDutyDate())).thenReturn(Optional.empty());
        when(dutyRepository.save(any(Duty.class))).thenReturn(duty);

        // when
        DutyResponse.DutyDTO result = dutyService.requestDuty(dutyRequest, userId);

        // then
        assertNotNull(result);
        assertEquals(duty.getId(), result.getId());
        assertEquals(user.getEmail(), result.getUserEmail());
        assertEquals(duty.getDutyDate(), result.getDutyDate());
        assertEquals(Status.PENDING, result.getStatus());

        verify(userRepository, times(1)).findById(userId);
        verify(dutyRepository, times(1)).findByUserAndDutyDate(user, duty.getDutyDate());
        verify(dutyRepository, times(1)).save(any(Duty.class));
    }

    @DisplayName("유저 당직 신청 실패 - 유효하지 않은 사용자 ID")
    @Test
    void requestDuty_Fail_InvalidUserId() {
        // given
        Long invalidUserId = 999L;
        DutyRequest.AddDTO dutyRequest = createDutyRequest("2023-07-01 00:00:00");

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> dutyService.requestDuty(dutyRequest, invalidUserId));

        verify(userRepository, times(1)).findById(invalidUserId);
        verify(dutyRepository, never()).findByUserAndDutyDate(any(User.class), any(Timestamp.class));
        verify(dutyRepository, never()).save(any(Duty.class));
    }

    @DisplayName("유저 당직 신청 실패 - 이미 당직이 있는 경우")
    @Test
    void requestDuty_Fail_ExistingDuty() {
        // given
        Long userId = 1L;
        DutyRequest.AddDTO dutyRequest = createDutyRequest("2023-07-01 00:00:00");

        User user = createUser(userId, "user1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Duty existingDuty = createDuty(1L, user, "2023-07-01 00:00:00");
        when(dutyRepository.findByUserAndDutyDate(user, existingDuty.getDutyDate())).thenReturn(Optional.of(existingDuty));

        // when, then
        assertThrows(Exception400.class, () -> dutyService.requestDuty(dutyRequest, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(dutyRepository, times(1)).findByUserAndDutyDate(user, existingDuty.getDutyDate());
        verify(dutyRepository, never()).save(any(Duty.class));
    }

    @DisplayName("유저 당직 취소 성공")
    @Test
    void cancelDuty_Success() {
        // given
        Long userId = 1L;
        Long dutyId = 1L;

        User user = createUser(userId, "user1");
        Duty duty = createDuty(dutyId, user, "2023-07-01 00:00:00");

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(duty));
        when(dutyRepository.save(any(Duty.class))).thenReturn(duty);

        // when
        DutyResponse.DutyDTO result = dutyService.cancelDuty(dutyId, userId);

        // then
        assertNotNull(result);
        assertEquals(duty.getId(), result.getId());
        assertEquals(user.getEmail(), result.getUserEmail());
        assertEquals(duty.getDutyDate(), result.getDutyDate());
        assertEquals(Status.CANCELLED, result.getStatus());

        verify(dutyRepository, times(1)).findById(dutyId);
        verify(dutyRepository, times(1)).save(any(Duty.class));
    }

    @DisplayName("유저 당직 취소 실패 - 유효하지 않은 당직 ID")
    @Test
    void cancelDuty_Fail_InvalidDutyId() {
        // given
        Long userId = 1L;
        Long invalidDutyId = 999L;

        when(dutyRepository.findById(invalidDutyId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> dutyService.cancelDuty(invalidDutyId, userId));

        verify(dutyRepository, times(1)).findById(invalidDutyId);
        verify(dutyRepository, never()).save(any(Duty.class));
    }

    @DisplayName("유저 당직 취소 실패 - 이미 승인된 당직 취소")
    @Test
    void cancelDuty_Fail_AlreadyApproved() {
        // given
        Long userId = 1L;
        Long dutyId = 1L;

        User user = createUser(userId, "user1");
        Duty approvedDuty = createDuty(dutyId, user, "2023-07-01 00:00:00");
        approvedDuty.updateStatus(Status.APPROVE);

        when(dutyRepository.findById(dutyId)).thenReturn(Optional.of(approvedDuty));

        // when, then
        assertThrows(Exception403.class, () -> dutyService.cancelDuty(dutyId, userId));

        verify(dutyRepository, times(1)).findById(dutyId);
        verify(dutyRepository, never()).save(any(Duty.class));
    }

    @DisplayName("내 당직 리스트(년도별 조회) 성공")
    @Test
    void getMyDutiesByYear_Success() {
        // given
        int year = 2023;
        Long userId = 1L;

        User user = createUser(userId, "user1");
        Duty duty1 = createDuty(1L, user, "2023-07-01 00:00:00");
        Duty duty2 = createDuty(2L, user, "2023-08-01 00:00:00");

        List<Duty> myDutiesInYear = Arrays.asList(duty1, duty2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dutyRepository.findByYearAndUser(year, user)).thenReturn(myDutiesInYear);

        // when
        List<DutyResponse.MyDutyDTO> result = dutyService.getMyDutiesByYear(year, userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(duty1.getId(), result.get(0).getId());
        assertEquals(duty2.getId(), result.get(1).getId());
        assertEquals(duty1.getDutyDate(), result.get(0).getDutyDate());
        assertEquals(duty2.getDutyDate(), result.get(1).getDutyDate());

        verify(userRepository, times(1)).findById(userId);
        verify(dutyRepository, times(1)).findByYearAndUser(year, user);
    }

    @DisplayName("내 당직 리스트(년도별 조회) 실패 - 유효하지 않은 사용자 ID")
    @Test
    void getMyDutiesByYear_Fail_InvalidUserId() {
        // given
        int year = 2023;
        Long invalidUserId = 999L;

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> dutyService.getMyDutiesByYear(year, invalidUserId));

        verify(userRepository, times(1)).findById(invalidUserId);
        verify(dutyRepository, never()).findByYearAndUser(anyInt(), any(User.class));
    }

    @DisplayName("전체 유저 당직 리스트(년도별 조회) 성공")
    @Test
    void getAllDutiesByYear_Success() {
        // given
        int year = 2023;

        User user1 = createUser(1L, "user1");
        User user2 = createUser(2L, "user2");

        Duty duty1 = createDuty(1L, user1, "2023-07-01 00:00:00");
        Duty duty2 = createDuty(2L, user2, "2023-07-10 00:00:00");

        List<Duty> duties = Arrays.asList(duty1, duty2);
        when(dutyRepository.findByDutyDateYear(year)).thenReturn(duties);

        when(encryption.decrypt(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        // when
        List<DutyResponse.ListDTO> result = dutyService.getAllDutiesByYear(year);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());

        verify(dutyRepository, times(1)).findByDutyDateYear(year);
        verify(encryption, times(4)).decrypt(anyString());
    }

    private DutyRequest.AddDTO createDutyRequest(String dutyDate) {
        return DutyRequest.AddDTO.builder()
                .dutyDate(Timestamp.valueOf(dutyDate))
                .build();
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