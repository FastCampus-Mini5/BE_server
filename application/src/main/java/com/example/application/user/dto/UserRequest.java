package com.example.application.user.dto;

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
    @Pattern(
        regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
        message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank
    @Size(min = 2, max = 15)
    private String username;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "비밀번호는 영어 대문자, 영어 소문자, 숫자, 특수문자를 모두 포함해야 하며, 최소 8글자 이상이어야 합니다.")
    private String password;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp hireDate;

    public SignUp toEntityEncrypted(PasswordEncoder passwordEncoder, Encryption encryption) {
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
  @ToString
  @Builder
  public static class SignInDTO {

    @NotBlank
    @Pattern(
        regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
        message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank private String password;
  }

  @Getter
  @ToString
  @Builder
  public static class CheckEmailDTO {
    @Setter
    @NotBlank
    @Email(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "유효하지 않은 이메일 주소입니다.")
    private String email;
  }

  @Getter
  @ToString
  @Builder
  public static class UpdateInfoDTO {
    private String profileImg; // TODO : 이미지 파일 첨부

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "비밀번호는 영어 대문자, 영어 소문자, 숫자, 특수문자를 모두 포함해야 하며, 최소 8글자 이상이어야 합니다.")
    private String password;
  }
}
