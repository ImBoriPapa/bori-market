package com.bmarket.securityservice.exception.error_code;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_ACCOUNT(1001,"계정을 찾을 수 없습니다."),
    FAIL_VALIDATION(2000,"검증에 실패했습니다"),

    DUPLICATE_LOGIN_ID(3001,"이미 사용중인 아이디는 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(3002,"이미 사용중인 닉네임은 사용할 수 없습니다."),
    DUPLICATE_EMAIL(3003,"이미 사용중인 이메일은 사용할 수 없습니다."),
    DUPLICATE_CONTACT(3004,"이미 사용중인 전화번호는 사용할 수 없습니다.")

    ;

    private int errorCode;
    private String errorMessage;

    ErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
