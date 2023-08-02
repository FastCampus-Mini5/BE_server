package com.example.admin.schedule.vacation.service;

import com.example.admin.schedule.vacation.dto.VacationRequest;
import com.example.admin.schedule.vacation.dto.VacationResponse;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Reason;
import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.schedule.VacationRepository;
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
class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;

    @Mock
    private VacationInfoRepository vacationInfoRepository;

    @InjectMocks
    private VacationService vacationService;

    @DisplayName("연차 목록 조회 성공")
    @Test
    void testGetVacationListSuccess() {
        //given
        String validStatus = "PENDING";
        Pageable pageable = mock(Pageable.class);

        List<User> testUsers = List.of(
                createUser(1L, "user1"),
                createUser(2L, "user2"),
                createUser(3L, "user3")
        );

        List<Vacation> vacationList = List.of(
                createVacation(1L, testUsers.get(0), "2023-07-01 00:00:00", "2023-07-05 00:00:00"),
                createVacation(2L, testUsers.get(1), "2023-07-02 00:00:00", "2023-07-03 00:00:00"),
                createVacation(3L, testUsers.get(2), "2023-07-03 00:00:00", "2023-07-04 00:00:00")
        );

        Page<Vacation> mockPage = new PageImpl<>(vacationList, pageable, vacationList.size());
        when(vacationRepository.findVacationsByStatus(pageable, Status.PENDING)).thenReturn(mockPage);

        //when
        Page<VacationResponse.ListDTO> result = vacationService.vacationListByStatus(pageable, validStatus);

        //then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.getTotalElements());
        verify(vacationRepository, times(1)).findVacationsByStatus(pageable, Status.PENDING);
    }

    @DisplayName("연차 목록 조회 실패 - 유효하지 않은 상태")
    @Test
    void testGetVacationListFailWithStatus() {
        //given
        String invalidStatus = "INVALID_STATUS";
        Pageable pageable = mock(Pageable.class);

        //when, then
        assertThrows(ValidStatusException.class,
                () -> vacationService.vacationListByStatus(pageable, invalidStatus));
        verify(vacationRepository, never()).findVacationsByStatus(any(), any());
    }

    @DisplayName("연차 목록 조회 실패 - 빈 페이지")
    @Test
    void testGetVacationListFailWithPage() {
        //given
        String validStatus = "APPROVED";
        Pageable pageable = null;

        //when, then
        assertThrows(EmptyPagingDataRequestException.class,
                () -> vacationService.vacationListByStatus(pageable, validStatus));
        verify(vacationRepository, never()).findVacationsByStatus(any(), any());
    }

    @DisplayName("연차 신청 승인 성공")
    @Test
    void testVacationApproveSuccess() {
        //given
        Long vacationId = 1L;
        VacationRequest.StatusDTO statusDTO = new VacationRequest.StatusDTO(vacationId, "APPROVE");

        User user = createUser(1L, "user1");
        Vacation existVacation = createVacation(vacationId, user, "2023-07-01 00:00:00", "2023-07-02 00:00:00");
        when(vacationRepository.findById(vacationId)).thenReturn(Optional.of(existVacation));
        VacationInfo vacationInfo = createVacationInfo(1L, user);
        when(vacationInfoRepository.findByUserId(user.getId())).thenReturn(Optional.of(vacationInfo));

        //when
        vacationService.updateStatus(statusDTO);

        //then
        verify(vacationRepository, times(1)).findById(vacationId);
        verify(vacationRepository, times(1)).save(existVacation);

        assertEquals(Status.APPROVE, existVacation.getStatus());
        verify(vacationInfoRepository, times(1)).findByUserId(any());
        verify(vacationInfoRepository, times(1)).save(any());
    }

    @DisplayName("연차 신청 승인 실패 - 유효하지 않은 상태")
    @Test
    void testVacationApproveFailWithStatus() {
        //given
        Long vacationId = 1L;
        VacationRequest.StatusDTO statusDTO = new VacationRequest.StatusDTO(vacationId, "INVALID_STATUS");

        //when, then
        assertThrows(ValidStatusException.class, () -> vacationService.updateStatus(statusDTO));
        verify(vacationRepository, never()).save(any());
    }

    @DisplayName("연차 신청 승인 실패 - 해당 연차 데이터가 없는 경우")
    @Test
    void testVacationApproveFailWithDTO() {
        //given
        Long vacationId = 1L;
        VacationRequest.StatusDTO statusDTO = new VacationRequest.StatusDTO(vacationId, "APPROVE");

        when(vacationRepository.findById(vacationId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(ScheduleServiceException.class, () -> vacationService.updateStatus(statusDTO));

        verify(vacationRepository, times(1)).findById(vacationId);
        verify(vacationRepository, never()).save(any());
        verify(vacationInfoRepository, never()).findByUserId(any());
        verify(vacationInfoRepository, never()).save(any());
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

    private Vacation createVacation(Long id, User user, String startDate, String endDate) {
        return Vacation.builder()
                .id(id)
                .user(user)
                .reason(Reason.반차)
                .status(Status.PENDING)
                .approvalDate(null)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .startDate(Timestamp.valueOf(startDate))
                .endDate(Timestamp.valueOf(endDate))
                .build();
    }

    private VacationInfo createVacationInfo(Long id, User user) {
        return VacationInfo.builder()
                .id(id)
                .user(user)
                .remainVacation(5)
                .usedVacation(3)
                .build();
    }
}
