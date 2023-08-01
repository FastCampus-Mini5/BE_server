package com.example.core.config._security;

import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception500;
import com.example.core.model.user.User;
import com.example.core.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("스프링 시큐리티 로그인 서비스 호출 email: " + email);

    Optional<User> userOptional = userRepository.findByEmail(email);
    User user = userOptional.orElseThrow(() -> new Exception500(ErrorMessage.LOGIN_FAILED));

    return new PrincipalUserDetail(user);
  }
}
