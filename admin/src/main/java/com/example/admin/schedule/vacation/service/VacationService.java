package com.example.admin.schedule.vacation.service;

import com.example.admin.schedule.vacation.dto.VacationRequest;
import com.example.admin.schedule.vacation.dto.VacationResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.ScheduleServiceException;
import com.example.core.errors.exception.ValidStatusException;
import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.schedule.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VacationService {

    private final VacationRepository vacationRepository;
    private final VacationInfoRepository vacationInfoRepository;
    private final Encryption encryption;

    @Transactional
    public void updateStatus(VacationRequest.StatusDTO statusDTO) {
        if (statusDTO == null) throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_VACATION);
        Status responseStatus = isValidStatus(statusDTO.getStatus());

        Long vacationId = statusDTO.getId();
        Optional<Vacation> optionalVacation = vacationRepository.findById(vacationId);
        Vacation vacation = optionalVacation.orElseThrow(
                () -> new ScheduleServiceException(ErrorMessage.NOT_FOUND_VACATION));

        vacation.updateStatus(responseStatus);
        vacationRepository.save(vacation);
        Long userId = vacation.getUser().getId();

        if (responseStatus == Status.APPROVE) {
            updateVacationInfoForApproval(userId);
        }
    }

    private void updateVacationInfoForApproval(Long userId) {
        VacationInfo vacationInfo = vacationInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new ScheduleServiceException(ErrorMessage.NOT_FOUND_VACATION));

        int remainVacation = vacationInfo.getRemainVacation() - 1;
        int userVacation = vacationInfo.getUsedVacation() - 1;
        vacationInfo.setRemainVacation(remainVacation);
        vacationInfo.setUsedVacation(userVacation);

        vacationInfoRepository.save(vacationInfo);
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
    public Page<VacationResponse.ListDTO> vacationListByStatus(Pageable pageable, String status) {
        if (pageable == null) throw new EmptyPagingDataRequestException();

        Status requestStatus = isValidStatus(status);
        Page<Vacation> vacationPage = vacationRepository.findVacationsByStatus(pageable, requestStatus);

        return vacationPage.map(VacationResponse.ListDTO::form).map(listDTO -> listDTO.decrypt(encryption));
    }
}
