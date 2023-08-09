package com.example.application.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.core.model.serverLog.ServerLog;
import com.example.core.model.user.User;
import com.example.core.repository.serverLog.ServerLogRepository;
import com.example.core.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[LoggingService] 로깅 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class LoggingServiceTest {
  @InjectMocks private LoggingService sut;
  @Mock UserRepository userRepository;

  @Mock private ServerLogRepository serverLogRepository;

  @DisplayName("[SUCCESS][logging] 로깅 호출 - 성공")
  @Test
  void givenUserEmailAndClientIp_whenRequestsLogging_thenSavesSigninLog() {
    // Given
    String userEmail = "testUser@test.com";
    String clientIp = "127.0.0.1";
    User user = User.builder().email(userEmail).build();
    given(userRepository.getReferenceByEmail(userEmail)).willReturn(user);
    ArgumentCaptor<ServerLog> serverLogArgumentCaptor = ArgumentCaptor.forClass(ServerLog.class);

    // When
    sut.logging(userEmail, clientIp);

    // Then
    then(serverLogRepository).should().save(serverLogArgumentCaptor.capture());
    ServerLog capturedServerLog = serverLogArgumentCaptor.getValue();
    assertEquals(user, capturedServerLog.getUser());
    assertEquals(clientIp, capturedServerLog.getRequestIp());
  }
}
