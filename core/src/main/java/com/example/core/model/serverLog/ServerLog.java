package com.example.core.model.serverLog;

import com.example.core.model.user.User;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "log_tb")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private String requestIp;

  @CreatedDate private Timestamp signInDate;
}
