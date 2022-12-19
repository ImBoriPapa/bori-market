package com.bmarket.apigatewayservice.exception.cutom_exception;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    NOT_FOUND_REASON(1000, "원인을 알 수 없는 에러입니다."),
    NOT_FOUND_ACCESS_TOKEN(1001, "ACCESS TOKEN을 확인할 수 없습니다."),
    EMPTY_ACCESS_TOKEN(1002, "ACCESS TOKEN이 필요한 요청입니다."),
    DENIED_ACCESS_TOKEN(1003, "ACCESS TOKEN이 거절 되었습니다."),
    EXPIRED_ACCESS_TOKEN(1005, "ACCESS TOKEN이 만료되었습니다.");
    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
