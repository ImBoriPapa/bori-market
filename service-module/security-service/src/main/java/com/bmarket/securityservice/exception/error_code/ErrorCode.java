package com.bmarket.securityservice.exception.error_code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUNT_REASON(1000,"원인을 알수 없는 에러가 발생했습니다."),
    NOT_FOUND_ACCOUNT(1001,"계정을 찾을 수 없습니다."),
    THIS_ACCOUNT_IS_LOGOUT(1002,"로그아웃된 계정입니다 다시 로그인해 주세요"),

    NOT_CORRECT_PASSWORD(1003,"비밀번호가 일치하지 않습니다"),
    FAIL_VALIDATION(2000,"검증에 실패했습니다"),
    FAIL_LOGIN(2001,"로그인에 실패했습니다. 아이디 혹은 비밀번호를 확인해주세요"),

    TOO_LONG_RANGE(2003,"검색은 최대 100건까지 검색할 수 있습니다"),
    EMPTY_ACCESS_TOKEN(2100,"토큰을 확인할 수 없습니다"),
    EXPIRED_REFRESH_TOKEN(2101,"리프레시 토큰이 만료 되었습니다. 다시 로그인해 주세요"),

    DENIED_ACCESS_TOKEN(2200,  "잘못된 토큰입니다."),
    DENIED_REFRESH_TOKEN(2201, "잘못된 토큰입니다."),
    DUPLICATE_LOGIN_ID(3001,"이미 사용중인 아이디는 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(3002,"이미 사용중인 닉네임은 사용할 수 없습니다."),
    DUPLICATE_EMAIL(3003,"이미 사용중인 이메일은 사용할 수 없습니다."),
    DUPLICATE_CONTACT(3004,"이미 사용중인 전화번호는 사용할 수 없습니다."),

    ACCESS_DENIED(3500, "접근 권한이 없습니다."),

    NOT_AUTHENTICATION_REQUEST(4000,"인증되지 않은 접근입니다."),

    TOKEN_IS_EMPTY(5000,"인증토큰이 없습니다."),
    TOKEN_IS_DENIED(5001,"인증토큰이 거절되었습니."),
    REFRESH_TOKEN_IS_EMPTY(5002,"리프레쉬 인증토큰이 없습니다."),
    REFRESH_TOKEN_IS_EXPIRED(5003,"리프레쉬 인증토큰이 만료되었습니다."),
    REFRESH_TOKEN_IS_DENIED(5004,"리프레쉬 인증토큰이 거절되었습니다.")




    ;

    private int errorCode;
    private String errorMessage;

    ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
