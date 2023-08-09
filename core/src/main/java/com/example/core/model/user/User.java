package com.example.core.model.user;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "user_tb")
@Entity
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(nullable = false, length = 100)
  @Setter
  private String password;

  @Column(nullable = false)
  private String role;

  @Lob
  @Column(nullable = false)
  @Setter
  private String profileImage;

  @Column(nullable = false)
  private Timestamp hireDate;

  @CreationTimestamp private Timestamp createdDate;

  @UpdateTimestamp private Timestamp updatedDate;

  @PrePersist
  protected void onCreate() {
    role = "user";
  }
}
