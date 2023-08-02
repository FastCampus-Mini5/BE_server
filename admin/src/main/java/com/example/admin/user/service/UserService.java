package com.example.admin.user.service;

import com.example.admin.user.dto.UserResponse;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.repository.schedule.VacationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final VacationInfoRepository vacationInfoRepository;

    public Page<UserResponse.ListDTO> getAllUsers(Pageable pageable) {
        if (pageable == null) throw new EmptyPagingDataRequestException();

        Page<VacationInfo> allUsersWithVacationInfo = vacationInfoRepository.findAll(pageable);
        return allUsersWithVacationInfo.map(UserResponse.ListDTO::from);
    }
}
