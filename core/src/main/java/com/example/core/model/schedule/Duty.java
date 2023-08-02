package com.example.core.model.schedule;

import com.example.core.model.user.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "on_duty_tb")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Duty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Timestamp dutyDate;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column private Timestamp approvalDate;

  @CreationTimestamp private Timestamp createdDate;

  @UpdateTimestamp private Timestamp updatedDate;

  @PrePersist
  protected void onCreate() {
    status = Status.PENDING;
  }

  public void updateStatus(Status status) {
    this.status = status;
    this.approvalDate = Timestamp.valueOf(LocalDateTime.now());
  }
}
