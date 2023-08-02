package com.example.core.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
  public static final String EMPTY_DATA_TO_SIGNUP = "회원 가입을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_SIGNIN= "로그인을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_CHECK_EMAIL = "중복 확인을 위한 이메일이 존재하지 않습니다.";

  public static final String UN_AUTHORIZED = "UNAUTH";
  public static final String FORBIDDEN = "FORBIDDEN";

  public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";

  public static final String EMPTY_DATA_TO_PAGING = "페이징을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_APPROVE_SIGNUP = "회원 가입 요청 승인을 위한 데이터가 존재하지 않습니다.";

  public static final String NOT_FOUND_SIGNUP = "회원 가입 요청 정보가 존재하지 않습니다.";

  public static final String INVALID_STATUS = "유효하지 않은 상태입니다.";

  public static final String NOT_FOUND_VACATION = "연차 승인 요청 정보가 존재하지 않습니다.";
  public static final String NOT_FOUND_DUTY = "당직 승인 요청 정보가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_VACATION = "연차 승인 요청 승인을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_DUTY = "당직 승인 요청 승인을 위한 데이터가 존재하지 않습니다.";

  public static final String ENCRYPT_ERROR = "암호화 오류: ";
  public static final String DECRYPT_ERROR = "복호화 오류: ";

  public static final String TOKEN_UN_AUTHORIZED = "UNAUTHORIZED";
  public static final String TOKEN_EXPIRED= "EXPIRED";
  public static final String TOKEN_VERIFICATION_FAILED= "VERIFICATION_FAILED";
}
