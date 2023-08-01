package com.example.core.model.user;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "sign_up_tb")
@Entity
public class SignUp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false)
  private Timestamp hireDate;

  @CreationTimestamp private Timestamp createdDate;

  public User toUser() {
    return User.builder()
        .email(email)
        .username(username)
        .password(password)
        .profileImage("/image/default.png")
        .hireDate(hireDate)
        .build();
  }
}
