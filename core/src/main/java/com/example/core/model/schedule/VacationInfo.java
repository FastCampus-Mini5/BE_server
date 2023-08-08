package com.example.core.model.schedule;

import javax.persistence.*;

import com.example.core.model.user.User;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "vacation_info_tb")
@Entity
public class VacationInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @Setter
  @Column(nullable = false)
  private int remainVacation;

  @Setter
  @Column(nullable = false)
  private int usedVacation;

  @PrePersist
  public void onCreate() {
    remainVacation = 0;
    usedVacation = 0;
  }

  public void increaseVacation() {
    if (remainVacation + usedVacation >= 15)
      return;
    remainVacation += 1;
  }
}