package com.example.admin.schedule;

import com.example.admin.schedule.duty.dto.DutyRequest;
import com.example.admin.schedule.duty.dto.DutyResponse;
import com.example.admin.schedule.duty.service.DutyService;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Duty;
import com.example.core.repository.schedule.DutyRepository;
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
public class DutyIntegrationTest {

    @Autowired
    private DutyService dutyService;

    @Autowired
    private DutyRepository dutyRepository;

    @DisplayName("당직 신청 승인 성공")
    @Test
    @Transactional
    void testDutyApproveSuccess() {
        // Given
        Long id = 1L;
        String status = "APPROVE";

        DutyRequest.StatusDTO statusDTO =
                new DutyRequest.StatusDTO(id, status);

        // When
        dutyService.updateByStatus(statusDTO);

        // Then
        Duty duty = dutyRepository.findById(id).orElse(null);
        Assertions.assertNotNull(duty);
        Assertions.assertEquals(status, duty.getStatus().toString());
    }

    @DisplayName("당직 신청 승인 실패 - 유효하지 않은 상태")
    @Test
    void testDutyApproveFailWithStatus() {
        // Given
        Long id = 1L;
        String status = "INVALID_STATUS";

        DutyRequest.StatusDTO statusDTO =
                new DutyRequest.StatusDTO(id, status);

        // When,Then
        assertThrows(ValidStatusException.class,
                () -> dutyService.updateByStatus(statusDTO));
    }

    @DisplayName("당직 신청 승인 실패 - 해당 당직 데이터가 없는 경우")
    @Test
    void testDutyApproveFailWithDTO() {
        // Given
        Long id = 7L;
        String status = "APPROVE";

        DutyRequest.StatusDTO statusDTO =
                new DutyRequest.StatusDTO(id, status);

        // When,Then
        assertThrows(ScheduleServiceException.class,
                () -> dutyService.updateByStatus(statusDTO));
    }

    @DisplayName("당직 목록 조회 성공")
    @Test
    void testGetDutyListSuccess() {
        // Given
        String status = "pending";
        Pageable pageable = PageRequest.of(0, 4);

        // When
        Page<DutyResponse.ListDTO> dutyPage = dutyService.dutyListByStatus(pageable, status);

        // Then
        Assertions.assertNotNull(dutyPage);
        Assertions.assertFalse(dutyPage.isEmpty());
        Assertions.assertEquals(4, dutyPage.getSize());
        Assertions.assertEquals(6, dutyPage.getTotalElements());
    }

    @DisplayName("당직 목록 조회 실패 - 유효하지 않은 상태")
    @Test
    void testGetDutyListFailWithStatus() {
        // Given
        String status = "INVALID_STATUS";
        Pageable pageable = PageRequest.of(0, 4);

        // When,Then
        assertThrows(ValidStatusException.class,
                () -> dutyService.dutyListByStatus(pageable, status));
    }

    @DisplayName("당직 목록 조회 실패 - 빈 페이지")
    @Test
    void testGetDutyListFailWithPage() {
        // Given
        String status = "pending";
        Pageable pageable = null;

        // When,Then
        assertThrows(EmptyPagingDataRequestException.class,
                () -> dutyService.dutyListByStatus(pageable, status));
    }
}
