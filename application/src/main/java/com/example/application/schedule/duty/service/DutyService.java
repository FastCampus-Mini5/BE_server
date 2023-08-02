package com.example.application.schedule.duty.service;

import com.example.application.schedule.duty.dto.DutyRequest;
import com.example.application.schedule.duty.dto.DutyResponse;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.Exception400;
import com.example.core.errors.exception.Exception403;
import com.example.core.errors.exception.Exception404;
import com.example.core.model.schedule.Duty;
import com.example.core.model.schedule.Status;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.DutyRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DutyService {

    private final DutyRepository dutyRepository;
    private final UserRepository userRepository;

    @Transactional
    public DutyResponse.DutyDTO requestDuty(DutyRequest.AddDTO dutyRequest, Long userId) {

        if (dutyRequest == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_REQUEST_DUTY);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        Duty duty = dutyRequest.toEntityWith(user);

        Optional<Duty> existingDuty = dutyRepository.findByUserAndDutyDate(duty.getUser(), duty.getDutyDate());
        existingDuty.ifPresent(existing -> {
            throw new Exception400(ErrorMessage.DUTY_ALREADY_EXISTS);
        });

        Duty savedDuty = dutyRepository.save(duty);
        return DutyResponse.DutyDTO.from(savedDuty);
    }

    @Transactional
    public DutyResponse.DutyDTO cancelDuty(DutyRequest.CancelDTO cancelDTO, Long userId) {

        if (cancelDTO == null) throw new Exception400(ErrorMessage.EMPTY_DATA_TO_CANCEL_DUTY);

        Long id = cancelDTO.getId();
        Duty duty = dutyRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorMessage.NOT_FOUND_DUTY));

        if (!duty.getUser().getId().equals(userId)) {
            throw new Exception403(ErrorMessage.UNAUTHORIZED_ACCESS_TO_DUTY);
        }

        if (duty.getStatus() == Status.APPROVE) {
            throw new Exception403(ErrorMessage.DUTY_CANNOT_BE_CANCELLED);
        }

        duty.updateStatus(Status.CANCELLED);
        Duty savedDuty = dutyRepository.save(duty);
        return DutyResponse.DutyDTO.from(savedDuty);
    }

    @Transactional(readOnly = true)
    public List<DutyResponse.MyDutyDTO> getMyDutiesByYear(int year, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        List<Duty> myDutiesInYear = dutyRepository.findByYearAndUser(year, user);
        return myDutiesInYear.stream().map(DutyResponse.MyDutyDTO::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DutyResponse.ListDTO> getAllDutiesByYear(int year, Pageable pageable) {

        if (pageable == null) throw new EmptyPagingDataRequestException();

        Page<Duty> allDutiesInYear = dutyRepository.findByDutyDateYear(year, pageable);
        return allDutiesInYear.map(DutyResponse.ListDTO::from);
    }
}