package com.example.admin.user.service;

import com.example.admin.user.dto.UserRequest;
import com.example.admin.user.dto.UserResponse;
import com.example.core.config._security.PrincipalUserDetail;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtTokenProvider;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.AdminSignInException;
import com.example.core.errors.exception.EmptyDtoRequestException;
import com.example.core.errors.exception.EmptyPagingDataRequestException;
import com.example.core.model.schedule.VacationInfo;
import com.example.core.model.user.User;
import com.example.core.repository.schedule.VacationInfoRepository;
import com.example.core.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final VacationInfoRepository vacationInfoRepository;
    private final Encryption encryption;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public Page<UserResponse.ListDTO> getAllUsers(Pageable pageable) {
        if (pageable == null) throw new EmptyPagingDataRequestException();

        Page<VacationInfo> allUsersWithVacationInfo = vacationInfoRepository.findAll(pageable);
        return allUsersWithVacationInfo
                .map(UserResponse.ListDTO::from)
                .map(listDTO -> listDTO.decrypt(encryption));
    }

    public UserResponse.SignInDTO signIn(UserRequest.SignInDTO signInDTO) {
        if (signInDTO == null) throw new EmptyDtoRequestException(ErrorMessage.EMPTY_DATA_TO_SIGNIN);

        final String encryptedEmail = encryption.encrypt(signInDTO.getEmail());
        userRepository.findByEmailAndRole(encryptedEmail, "ADMIN").orElseThrow(() ->
                new AdminSignInException(ErrorMessage.INVALID_ADMIN_EMAIL));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(encryptedEmail, signInDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);
        PrincipalUserDetail userDetail = (PrincipalUserDetail) authentication.getPrincipal();
        final User user = userDetail.getUser();

        String jwt = JwtTokenProvider.create(user);
        return new UserResponse.SignInDTO(jwt);
    }
}
