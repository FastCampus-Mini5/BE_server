package com.example.admin.user.service;

import com.example.admin.user.dto.SignUpRequest;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.SignUpServiceException;
import com.example.core.model.user.SignUp;
import com.example.core.model.user.User;
import com.example.core.repository.user.SignUpRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final SignUpRepository signUpRepository;
    private final UserRepository userRepository;

    @Transactional
    public void approve(SignUpRequest.ApproveDTO approveDTO) {
        if (approveDTO == null) throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_APPROVE_SIGNUP);

        Optional<SignUp> signUpOptional = signUpRepository.findByEmail(approveDTO.getEmail());
        SignUp signUp = signUpOptional.orElseThrow(() -> new SignUpServiceException(ErrorMessage.NOT_FOUND_SIGNUP));

        User user = signUp.toUser();
        userRepository.save(user);
        signUpRepository.delete(signUp);
    }
}