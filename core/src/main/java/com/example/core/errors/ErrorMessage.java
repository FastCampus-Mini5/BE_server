package com.example.core.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
  public static final String EMPTY_DATA_TO_SIGNUP = "회원 가입을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_SIGNIN = "로그인을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_CHECK_EMAIL = "중복 확인을 위한 이메일이 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_FIND_PASSWORD = "비밀번호 찾기를 위한 데이터가 존재하지 않습니다.";

  public static final String UN_AUTHORIZED = "인증되지 않았습니다.";
  public static final String FORBIDDEN = "접근이 거부되었습니다.";

  public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";
  public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";

  public static final String EMPTY_DATA_TO_PAGING = "페이징을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_APPROVE_SIGNUP = "회원 가입 요청 승인을 위한 데이터가 존재하지 않습니다.";

  public static final String NOT_FOUND_SIGNUP = "회원 가입 요청 정보가 존재하지 않습니다.";
  public static final String NOT_FOUND_USER_TO_RESET_PASSWORD = "입력하신 이메일에 해당하는 회원 정보가 존재하지 않습니다.";

  public static final String INVALID_STATUS = "유효하지 않은 상태입니다.";

  public static final String NOT_FOUND_VACATION = "연차 승인 요청 정보가 존재하지 않습니다.";
  public static final String NOT_FOUND_DUTY = "당직 승인 요청 정보가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_VACATION = "연차 승인 요청 승인을 위한 데이터가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_DUTY = "당직 승인 요청 승인을 위한 데이터가 존재하지 않습니다.";

  public static final String ENCRYPT_ERROR = "암호화 오류: ";
  public static final String DECRYPT_ERROR = "복호화 오류: ";

  public static final String TOKEN_UN_AUTHORIZED = "토큰 인증에 실패하였습니다.";
  public static final String TOKEN_EXPIRED = "토큰이 만료되었습니다.";
  public static final String TOKEN_VERIFICATION_FAILED = "올바른 토큰이 아닙니다.";

  public static final String EMPTY_DATA_TO_REQUEST_VACATION = "연차 신청 정보가 없습니다.";
  public static final String VACATION_INFO_NOT_FOUND = "연차 정보를 찾을 수 없습니다.";
  public static final String NOT_ENOUGH_REMAINING_VACATION_DAYS = "남은 연차일이 충분하지 않습니다.";
  public static final String VACATION_NOT_FOUND = "요청한 연차가 존재하지 않습니다.";
  public static final String EMPTY_DATA_TO_CANCEL_VACATION = "연차 취소 정보가 없습니다.";
  public static final String UNAUTHORIZED_ACCESS_TO_VACATION = "연차에 대한 접근 권한이 없습니다.";
  public static final String VACATION_CANNOT_BE_CANCELLED = "이미 승인된 연차는 취소할 수 없습니다.";

  public static final String DUTY_ALREADY_EXISTS = "해당 날짜에 이미 당직이 있습니다.";
  public static final String EMPTY_DATA_TO_REQUEST_DUTY = "당직 신청 정보가 없습니다.";
  public static final String EMPTY_DATA_TO_CANCEL_DUTY = "당직 취소 정보가 없습니다.";
  public static final String UNAUTHORIZED_ACCESS_TO_DUTY = "당직에 대한 접근 권한이 없습니다.";
  public static final String DUTY_CANNOT_BE_CANCELLED = "이미 승인된 당직은 취소할 수 없습니다.";

  public static final String EMAIL_SEND_FAILED = "이메일 전송 중 오류가 발생했습니다.";
  public static final String INVALID_EMAIL = "유효하지 않은 이메일 주소입니다.";
  public static final String INVALID_PASSWORD =
      "영어 대문자, 소문자, 숫자, 특수문자 모두 포함. 최소 8글자 이상.";
  public static final String INVALID_ADMIN_EMAIL = "유효하지 않은 관리자 정보입니다.";

  public static final String DUPLICATED_EMAIL = "해당 이메일의 회원 가입 요청이 존재합니다.";
}
