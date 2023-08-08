package com.example.application.schedule.vacation.service;

import com.example.application.schedule.vacation.dto.VacationRequest;
import com.example.application.schedule.vacation.dto.VacationResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception403;
import com.example.core.errors.exception.Exception404;
import com.example.core.model.schedule.Reason;
import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.schedule.VacationRepository;
import com.example.core.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;

    @Mock
    private VacationInfoRepository vacationInfoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VacationService vacationService;

    @Mock
    private Encryption encryption;

    @DisplayName("유저 연차 신청 성공")
    @Test
    void requestVacation_Success() {
        // given
        Long userId = 1L;
        VacationRequest.AddDTO vacationRequest = createVacationRequest("2023-07-01 00:00:00", "2023-07-03 00:00:00");

        User user = createUser(userId, "user1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Vacation vacation = createVacation(1L, user, "2023-07-01 00:00:00", "2023-07-03 00:00:00");
        when(vacationRepository.save(any(Vacation.class))).thenReturn(vacation);

        VacationInfo vacationInfo = createVacationInfo(user, 15, 5);
        when(vacationInfoRepository.findByUser(user)).thenReturn(Optional.of(vacationInfo));

        // when
        VacationResponse.VacationDTO result = vacationService.requestVacation(vacationRequest, userId);

        // then
        assertNotNull(result);
        assertEquals(vacation.getId(), result.getId());
        assertEquals(user.getEmail(), result.getUserEmail());
        assertEquals(vacation.getReason(), result.getReason());
        assertEquals(vacation.getStatus(), result.getStatus());
        assertEquals(vacation.getStartDate(), result.getStartDate());
        assertEquals(vacation.getEndDate(), result.getEndDate());
        assertNull(result.getApprovalDate());

        verify(userRepository, times(1)).findById(userId);
        verify(vacationRepository, times(1)).save(any(Vacation.class));
        verify(vacationInfoRepository, times(1)).findByUser(user);
    }

    @DisplayName("유저 연차 신청 실패 - 유효하지 않은 사용자 ID")
    @Test
    void requestVacation_Fail_InvalidUserId() {
        // given
        Long invalidUserId = 999L;
        VacationRequest.AddDTO vacationRequest = createVacationRequest("2023-07-01 00:00:00", "2023-07-03 00:00:00");

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> vacationService.requestVacation(vacationRequest, invalidUserId));

        verify(userRepository, times(1)).findById(invalidUserId);
        verify(vacationRepository, never()).save(any(Vacation.class));
        verify(vacationInfoRepository, never()).findByUser(any(User.class));
        verify(vacationInfoRepository, never()).save(any(VacationInfo.class));
    }

    @DisplayName("유저 연차 신청 실패 - 부족한 남은 연차 일수")
    @Test
    void requestVacation_Fail_NotEnoughRemainingDays() {
        // given
        Long userId = 1L;
        VacationRequest.AddDTO vacationRequest = createVacationRequest("2023-07-01 00:00:00", "2023-07-15 00:00:00");

        User user = createUser(userId, "user1");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        VacationInfo vacationInfo = createVacationInfo(user, 15, 10);
        when(vacationInfoRepository.findByUser(user)).thenReturn(Optional.of(vacationInfo));

        // when, then
        assertThrows(Exception400.class, () -> vacationService.requestVacation(vacationRequest, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(vacationRepository, never()).save(any(Vacation.class));
        verify(vacationInfoRepository, times(1)).findByUser(user);
        verify(vacationInfoRepository, never()).save(any(VacationInfo.class));
    }

    @DisplayName("유저 연차 취소 성공")
    @Test
    void cancelVacation_Success() {
        // given
        Long userId = 1L;
        Long vacationId = 1L;

        User user = createUser(userId, "user1");
        Vacation vacation = createVacation(vacationId, user, "2023-07-01 00:00:00", "2023-07-03 00:00:00", Status.PENDING);

        when(vacationRepository.findById(vacationId)).thenReturn(Optional.of(vacation));
        when(vacationRepository.save(any(Vacation.class))).thenReturn(vacation);

        // when
        VacationResponse.VacationDTO result = vacationService.cancelVacation(vacationId, userId);

        // then
        assertNotNull(result);
        assertEquals(vacation.getId(), result.getId());
        assertEquals(user.getEmail(), result.getUserEmail());
        assertEquals(vacation.getReason(), result.getReason());
        assertEquals(Status.CANCELLED, result.getStatus());
        assertEquals(vacation.getStartDate(), result.getStartDate());
        assertEquals(vacation.getEndDate(), result.getEndDate());
        assertEquals(vacation.getApprovalDate(), result.getApprovalDate());

        verify(vacationRepository, times(1)).findById(vacationId);
        verify(vacationRepository, times(1)).save(any(Vacation.class));
    }

    @DisplayName("유저 연차 취소 실패 - 존재하지 않는 연차 ID")
    @Test
    void cancelVacation_Fail_InvalidVacationId() {
        // given
        Long invalidVacationId = 999L;

        when(vacationRepository.findById(invalidVacationId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> vacationService.cancelVacation(invalidVacationId, 1L));

        verify(vacationRepository, times(1)).findById(invalidVacationId);
    }

    @DisplayName("유저 연차 취소 실패 - 이미 승인된 연차 취소")
    @Test
    void cancelVacation_Fail_ApprovedVacation() {
        // given
        Long userId = 1L;
        Long vacationId = 1L;

        User user = createUser(userId, "user1");
        Vacation vacation = createVacation(vacationId, user, "2023-07-01 00:00:00", "2023-07-03 00:00:00", Status.APPROVE);
        when(vacationRepository.findById(vacationId)).thenReturn(Optional.of(vacation));

        // when, then
        assertThrows(Exception403.class, () -> vacationService.cancelVacation(vacationId, userId));

        verify(vacationRepository, times(1)).findById(vacationId);
    }

    @DisplayName("내 연차 리스트(년도별 조회) 성공")
    @Test
    void getMyVacationsByYear_Success() {
        // given
        int year = 2023;
        Long userId = 1L;

        User user = createUser(userId, "user1");
        Vacation vacation1 = createVacation(1L, user, "2023-07-01 00:00:00", "2023-07-05 00:00:00");
        Vacation vacation2 = createVacation(2L, user, "2023-08-01 00:00:00", "2023-08-05 00:00:00");

        List<Vacation> myVacationsInYear = Arrays.asList(vacation1, vacation2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(vacationRepository.findByYearAndUser(year, user)).thenReturn(myVacationsInYear);

        // when
        List<VacationResponse.MyVacationDTO> result = vacationService.getMyVacationsByYear(year, userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(vacation1.getId(), result.get(0).getId());
        assertEquals(vacation2.getId(), result.get(1).getId());
        assertEquals(vacation1.getStartDate(), result.get(0).getStartDate());
        assertEquals(vacation2.getStartDate(), result.get(1).getStartDate());

        verify(userRepository, times(1)).findById(userId);
        verify(vacationRepository, times(1)).findByYearAndUser(year, user);
    }

    @DisplayName("내 연차 리스트(년도별 조회) 실패 - 유효하지 않은 사용자 ID")
    @Test
    void getMyVacationsByYear_Fail_InvalidUserId() {
        // given
        int year = 2023;
        Long invalidUserId = 999L;

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(Exception404.class, () -> vacationService.getMyVacationsByYear(year, invalidUserId));

        verify(userRepository, times(1)).findById(invalidUserId);
        verify(vacationRepository, never()).findByYearAndUser(anyInt(), any(User.class));
    }

    @DisplayName("전체 유저 연차 리스트(년도별 조회) 성공")
    @Test
    void getAllVacationsByYear_Success() {
        // given
        int year = 2023;

        User user1 = createUser(1L, "user1");
        User user2 = createUser(2L, "user2");

        Vacation vacation1 = createVacation(1L, user1, "2023-07-01 00:00:00", "2023-07-03 00:00:00");
        Vacation vacation2 = createVacation(2L, user2, "2023-07-10 00:00:00", "2023-07-12 00:00:00");

        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        when(vacationRepository.findByStartDateYear(year)).thenReturn(vacations);

        when(encryption.decrypt(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        // when
        List<VacationResponse.ListDTO> result = vacationService.getAllVacationsByYear(year);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());

        verify(vacationRepository, times(1)).findByStartDateYear(year);
        verify(encryption, times(4)).decrypt(anyString());
    }

    private Vacation createVacation(Long id, User user, String startDate, String endDate, Status status) {
        return Vacation.builder()
                .id(id)
                .user(user)
                .reason(Reason.연차)
                .status(status)
                .startDate(Timestamp.valueOf(startDate))
                .endDate(Timestamp.valueOf(endDate))
                .build();
    }

    private VacationRequest.AddDTO createVacationRequest(String startDate, String endDate) {
        return VacationRequest.AddDTO.builder()
                .reason(Reason.휴가)
                .startDate(Timestamp.valueOf(startDate))
                .endDate(Timestamp.valueOf(endDate))
                .build();
    }

    private User createUser(Long id, String username) {
        return User.builder()
                .id(id)
                .email("test" + id + "@example.com")
                .username(username)
                .password("password123")
                .role("user")
                .profileImage("profile" + id + ".jpg")
                .hireDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private Vacation createVacation(Long id, User user, String startDate, String endDate) {
        return Vacation.builder()
                .id(id)
                .user(user)
                .reason(Reason.기타사항)
                .status(Status.PENDING)
                .startDate(Timestamp.valueOf(startDate))
                .endDate(Timestamp.valueOf(endDate))
                .build();
    }

    private VacationInfo createVacationInfo(User user, int totalVacation, int usedVacation) {
        return VacationInfo.builder()
                .user(user)
                .remainVacation(totalVacation - usedVacation)
                .usedVacation(usedVacation)
                .build();
    }
}
