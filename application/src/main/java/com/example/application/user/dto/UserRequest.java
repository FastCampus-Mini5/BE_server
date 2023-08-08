package com.example.application.user.dto;

import static com.example.core.errors.ErrorMessage.INVALID_EMAIL;
import static com.example.core.errors.ErrorMessage.INVALID_PASSWORD;

import com.example.core.config._security.encryption.Encryption;
import com.example.core.model.user.SignUp;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import javax.validation.constraints.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  public static class SignUpDTO {

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = INVALID_EMAIL)
    private String email;

    @NotBlank
    @Size(min = 2, max = 15)
    private String username;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = INVALID_PASSWORD)
    private String password;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp hireDate;

    public SignUp toEncryptedEntity(PasswordEncoder passwordEncoder, Encryption encryption) {
      String encryptedUsername = encryption.encrypt(username);
      String encodedPassword = passwordEncoder.encode(this.password);
      String encryptedEmail = encryption.encrypt(email);

      return SignUp.builder()
          .username(encryptedUsername)
          .password(encodedPassword)
          .email(encryptedEmail)
          .hireDate(hireDate)
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  public static class SignInDTO {

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = INVALID_EMAIL)
    private String email;

    @NotBlank private String password;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  public static class CheckEmailDTO {
    @Setter
    @NotBlank
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = INVALID_EMAIL)
    private String email;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  public static class UpdateInfoDTO {
    private String profileImage;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = INVALID_PASSWORD)
    private String password;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ToString
  public static class ResetPasswordDTO {
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = INVALID_EMAIL)
    private String email;
  }
}
