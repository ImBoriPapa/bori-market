package com.bmarket.securityservice.utils.status;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    //1000~ 성공
    SUCCESS(1000, "성공"),
    REQUEST_SUCCESS(1001, "요청이 성공 했습니다."),
    //4000~ 에러
    ERROR(4000, "에러 발생"),

    NOT_FOUNT_REASON(4000, "원인을 알수 없는 에러가 발생했습니다."),
    NOT_FOUND_ACCOUNT(4001, "계정을 찾을 수 없습니다."),
    THIS_ACCOUNT_IS_LOGOUT(4002, "로그아웃된 계정입니다 다시 로그인해 주세요"),

    NOT_CORRECT_PASSWORD(4003, "비밀번호가 일치하지 않습니다"),
    FAIL_VALIDATION(4004, "검증에 실패했습니다"),
    FAIL_LOGIN(4005, "로그인에 실패했습니다. 아이디 혹은 비밀번호를 확인해주세요"),

    TOO_LONG_RANGE(4006, "검색은 최대 100건까지 검색할 수 있습니다"),
    EMPTY_ACCESS_TOKEN(4007, "토큰을 확인할 수 없습니다"),
    EXPIRED_REFRESH_TOKEN(4008, "리프레시 토큰이 만료 되었습니다. 다시 로그인해 주세요"),

    INVALID_ACCESS_TOKEN(4009, "잘못된  토큰입니다."),
    INVALID_REFRESH_TOKEN(4010, "잘못된 토큰입니다."),
    DUPLICATE_LOGIN_ID(4011, "이미 사용중인 아이디는 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(4012, "이미 사용중인 닉네임은 사용할 수 없습니다."),
    DUPLICATE_EMAIL(4013, "이미 사용중인 이메일은 사용할 수 없습니다."),
    DUPLICATE_CONTACT(4014, "이미 사용중인 전화번호는 사용할 수 없습니다."),

    ACCESS_DENIED(4015, "접근 권한이 없습니다."),

    NOT_AUTHENTICATION_REQUEST(4016, "인증되지 않은 접근입니다."),

    TOKEN_IS_EMPTY(4017, "인증토큰이 없습니다."),
    TOKEN_IS_DENIED(4018, "인증토큰이 거절되었습니."),
    REFRESH_TOKEN_IS_EMPTY(4019, "리프레쉬 인증토큰이 없습니다."),
    REFRESH_TOKEN_IS_EXPIRED(4020, "리프레쉬 인증토큰이 만료되었습니다."),
    REFRESH_TOKEN_IS_DENIED(4021, "리프레쉬 인증토큰이 거절되었습니다.");
    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
