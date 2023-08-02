package com.example.application.user.dto;

import lombok.Builder;
import lombok.Getter;

public class UserResponse {

  @Getter
  @Builder
  public static class AvailableEmailDTO {
    private String email;
    private Boolean available;
  }
}
