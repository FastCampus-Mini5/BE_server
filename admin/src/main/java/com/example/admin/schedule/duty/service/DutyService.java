package com.example.admin.schedule.duty.service;

import com.example.admin.schedule.duty.dto.DutyRequest;
import com.example.admin.schedule.duty.dto.DutyResponse;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.repository.schedule.DutyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DutyService {

    private final DutyRepository dutyRepository;

    @Transactional
    public void updateByStatus(DutyRequest.StatusDTO statusDTO) {
        if (statusDTO == null) throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_DUTY);

        Long id = statusDTO.getId();
        Duty duty = dutyRepository.findById(id).orElseThrow(
                () -> new ScheduleServiceException(ErrorMessage.NOT_FOUND_DUTY));

        String responseStatus = statusDTO.getStatus();
        Status updatedStatus = isValidStatus(responseStatus);

        duty.updateStatus(updatedStatus);
        dutyRepository.save(duty);
    }

    private Status isValidStatus(String status) {
        if (status == null) throw new ValidStatusException();

        try {
            Status validStatus = Status.valueOf(status.toUpperCase());
            return validStatus;
        } catch (IllegalArgumentException e) {
            throw new ValidStatusException();
        }
    }

    @Transactional(readOnly = true)
    public Page<DutyResponse.ListDTO> dutyListByStatus(Pageable pageable, String status) {
        if (pageable == null) throw new EmptyPagingDataRequestException();
        Status requestStatus = isValidStatus(status);
        Page<Duty> dutyPage = dutyRepository.findDutyByStatus(pageable, requestStatus);
        return dutyPage.map(DutyResponse.ListDTO::form);
    }
}
