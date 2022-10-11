package com.bmarket.securityservice.utils.status;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    SUCCESS(1000,"성공"),
    REQUEST_SUCCESS(1001,"요청이 성공 했습니다."),

    ERROR(4000,"에러 발생");
    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
