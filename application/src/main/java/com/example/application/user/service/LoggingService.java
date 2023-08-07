package com.example.application.user.service;

import com.example.core.model.serverLog.ServerLog;
import com.example.core.model.user.User;
import com.example.core.repository.serverLog.ServerLogRepository;
import com.example.core.repository.user.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoggingService {
  private final UserRepository userRepository;
  private final ServerLogRepository serverLogRepository;

  public void logging(String userEmail, String clientIp) {
    User user = userRepository.getReferenceByEmail(userEmail);

    ServerLog serverLog =
        ServerLog.builder()
            .user(user)
            .requestIp(clientIp)
            .signInDate(Timestamp.valueOf(LocalDateTime.now()))
            .build();

    serverLogRepository.save(serverLog);
  }
}
