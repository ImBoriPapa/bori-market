package com.bmarket.securityservice.exception.error_code;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_ACCOUNT(1001,"계정을 찾을 수 없습니다."),

    THIS_ACCOUNT_IS_LOGOUT(1002,"로그아웃된 계정입니다 다시 로그인해 주세요"),
    FAIL_VALIDATION(2000,"검증에 실패했습니다"),

    FAIL_LOGIN(2001,"로그인에 실패했습니다. 아이디 혹은 비밀번호를 확인해주세요"),

    DUPLICATE_LOGIN_ID(3001,"이미 사용중인 아이디는 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(3002,"이미 사용중인 닉네임은 사용할 수 없습니다."),
    DUPLICATE_EMAIL(3003,"이미 사용중인 이메일은 사용할 수 없습니다."),
    DUPLICATE_CONTACT(3004,"이미 사용중인 전화번호는 사용할 수 없습니다."),

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
