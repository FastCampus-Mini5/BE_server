package com.example.application.schedule.vacation.service;

import com.example.application.schedule.vacation.dto.VacationRequest;
import com.example.application.schedule.vacation.dto.VacationResponse;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception403;
import com.example.core.errors.exception.Exception404;
import com.example.core.model.schedule.Status;
import com.example.core.model.schedule.Vacation;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.schedule.VacationRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VacationService {

    private final UserRepository userRepository;
    private final VacationRepository vacationRepository;
    private final VacationInfoRepository vacationInfoRepository;

    @Transactional
    public VacationResponse.VacationDTO requestVacation(VacationRequest.AddDTO vacationRequest, Long userId) {

        if (vacationRequest == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_REQUEST_VACATION);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        Vacation vacation = vacationRequest.toEntityWith(user);

        VacationInfo vacationInfo = vacationInfoRepository.findByUser(user)
                .orElseThrow(() -> new Exception404(ErrorMessage.VACATION_INFO_NOT_FOUND));

        long vacationDays = Duration.between(vacation.getStartDate().toLocalDateTime(),
                vacation.getEndDate().toLocalDateTime()).toDays();

        if (vacationInfo.getRemainVacation() < vacationDays) {
            throw new Exception400(ErrorMessage.NOT_ENOUGH_REMAINING_VACATION_DAYS);
        }

        int updatedRemainVacation = vacationInfo.getRemainVacation() - (int) vacationDays;
        vacationInfo.setRemainVacation(updatedRemainVacation);

        Vacation savedVacation = vacationRepository.save(vacation);
        return VacationResponse.VacationDTO.from(savedVacation);
    }

    @Transactional
    public VacationResponse.VacationDTO cancelVacation(VacationRequest.CancelDTO cancelDTO, Long userId) {

        if (cancelDTO == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_CANCEL_VACATION);

        Long id = cancelDTO.getId();
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorMessage.VACATION_NOT_FOUND));

        if (!vacation.getUser().getId().equals(userId)) {
            throw new Exception403(ErrorMessage.UNAUTHORIZED_ACCESS_TO_VACATION);
        }

        if (vacation.getStatus() == Status.APPROVE) {
            throw new Exception403(ErrorMessage.VACATION_CANNOT_BE_CANCELLED);
        }

        vacation.updateStatus(Status.CANCELLED);
        Vacation savedVacation = vacationRepository.save(vacation);
        return VacationResponse.VacationDTO.from(savedVacation);
    }

    @Transactional(readOnly = true)
    public List<VacationResponse.MyVacationDTO> getMyVacationsByYear(int year, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        List<Vacation> myVacationsInYear = vacationRepository.findByYearAndUser(year, user);
        return myVacationsInYear.stream().map(VacationResponse.MyVacationDTO::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<VacationResponse.ListDTO> getAllVacationsByYear(int year, Pageable pageable) {

        if (pageable == null) throw new EmptyPagingDataRequestException();

        Page<Vacation> allVacationsInYear = vacationRepository.findByStartDateYear(year, pageable);
        return allVacationsInYear.map(VacationResponse.ListDTO::from);
    }
}