package com.example.admin.schedule;

import com.example.admin.schedule.vacation.dto.VacationRequest;
import com.example.admin.schedule.vacation.dto.VacationResponse;
import com.example.admin.schedule.vacation.service.VacationService;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Vacation;
import com.example.core.repository.schedule.VacationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test/.env")
public class VacationIntegrationTest {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private VacationRepository vacationRepository;

    @DisplayName("연차 신청 승인 성공")
    @Test
    @Transactional
    void testVacationApproveSuccess() {
        // Given
        Long id = 1L;
        String status = "APPROVE";

        VacationRequest.StatusDTO statusDTO =
                new VacationRequest.StatusDTO(id, status);

        // When
        vacationService.updateStatus(statusDTO);

        // Then
        Vacation vacation = vacationRepository.findById(id).orElse(null);
        Assertions.assertNotNull(vacation);
        Assertions.assertEquals(status, vacation.getStatus().toString());
    }

    @DisplayName("연차 신청 승인 실패 - 유효하지 않은 상태")
    @Test
    void testVacationApproveFailWithStatus() {
        // Given
        Long id = 1L;
        String status = "INVALID_STATUS";

        VacationRequest.StatusDTO statusDTO =
                new VacationRequest.StatusDTO(id, status);

        // When,Then
        assertThrows(ValidStatusException.class,
                () -> vacationService.updateStatus(statusDTO));
    }

    @DisplayName("연차 신청 승인 실패 - 해당 연차 데이터가 없는 경우")
    @Test
    void testVacationApproveFailWithDTO() {
        // Given
        Long id = 11L;
        String status = "APPROVE";

        VacationRequest.StatusDTO statusDTO =
                new VacationRequest.StatusDTO(id, status);

        // When,Then
        assertThrows(ScheduleServiceException.class,
                () -> vacationService.updateStatus(statusDTO));
    }

    @DisplayName("연차 목록 조회 성공")
    @Test
    void testGetVacationListSuccess() {
        // Given
        String status = "pending";
        Pageable pageable = PageRequest.of(0, 4);

        // When
        Page<VacationResponse.ListDTO> vacationPage = vacationService.vacationListByStatus(pageable, status);

        // Then
        Assertions.assertNotNull(vacationPage);
        Assertions.assertFalse(vacationPage.isEmpty());
        Assertions.assertEquals(4, vacationPage.getSize());
        Assertions.assertEquals(5, vacationPage.getTotalElements());
    }

    @DisplayName("연차 목록 조회 실패 - 유효하지 않은 상태")
    @Test
    void testGetVacationListFailWithStatus() {
        // Given
        String status = "INVALID_STATUS";
        Pageable pageable = PageRequest.of(0, 4);

        // When,Then
        assertThrows(ValidStatusException.class,
                () -> vacationService.vacationListByStatus(pageable, status));
    }

    @DisplayName("연차 목록 조회 실패 - 빈 페이지")
    @Test
    void testGetVacationListFailWithPage() {
        // Given
        String status = "pending";
        Pageable pageable = null;

        // When,Then
        assertThrows(EmptyPagingDataRequestException.class,
                () -> vacationService.vacationListByStatus(pageable, status));
    }
}
