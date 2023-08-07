package com.example.admin.user.service;

import com.example.admin.user.dto.SignUpRequest;
import com.example.admin.user.dto.SignUpResponse;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.errors.exception.SignUpServiceException;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final SignUpRepository signUpRepository;
    private final UserRepository userRepository;
    private final Encryption encryption;
    private final VacationInfoRepository vacationInfoRepository;

    @Transactional
    public void approve(SignUpRequest.ApproveDTO approveDTO) {
        if (approveDTO == null) throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_APPROVE_SIGNUP);

        String encryptedEmail = encryption.encrypt(approveDTO.getEmail());
        Optional<SignUp> signUpOptional = signUpRepository.findByEmail(encryptedEmail);
        SignUp signUp = signUpOptional.orElseThrow(() -> new SignUpServiceException(ErrorMessage.NOT_FOUND_SIGNUP));

        final User user = signUp.toUser();
        final VacationInfo vacationInfo = VacationInfo.builder().user(user).build();

        userRepository.save(user);
        signUpRepository.delete(signUp);
        vacationInfoRepository.save(vacationInfo);
    }

    @Transactional(readOnly = true)
    public Page<SignUpResponse.ListDTO> getAllList(Pageable pageable) throws EmptyPagingDataRequestException {
        if (pageable == null) throw new EmptyPagingDataRequestException();

        Page<SignUp> signUps = signUpRepository.findAll(pageable);
        return signUps
                .map(SignUpResponse.ListDTO::from)
                .map(listDTO -> listDTO.decrypt(encryption));
    }
}
