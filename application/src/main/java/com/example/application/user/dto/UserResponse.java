package com.example.application.user.dto;

import com.example.core.config._security.encryption.Encryption;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

public class UserResponse {

  @Getter
  @Builder
  public static class AvailableEmailDTO {
    private String email;
    private Boolean available;
  }

  @Getter
  @Builder
  public static class UserInfoDTO {
    private String username;
    private String email;
    private String profileImage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp hireDate;

    public UserInfoDTO toDecryptedDTO(Encryption encryption) {
      String decryptedUsername = encryption.decrypt(username);
      String decryptedEmail = encryption.decrypt(email);

      return UserInfoDTO.builder()
          .username(decryptedUsername)
          .email(decryptedEmail)
          .profileImage(profileImage)
          .hireDate(hireDate)
          .build();
    }
  }
}
