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

    DUPLICATE_LOGIN_ID(4011, "이미 사용중인 아이디는 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(4012, "이미 사용중인 닉네임은 사용할 수 없습니다."),
    DUPLICATE_EMAIL(4013, "이미 사용중인 이메일은 사용할 수 없습니다."),
    DUPLICATE_CONTACT(4014, "이미 사용중인 전화번호는 사용할 수 없습니다."),

    ACCESS_DENIED(4015, "접근 권한이 없습니다."),

    NOT_AUTHENTICATION_REQUEST(4016, "인증되지 않은 접근입니다."),

    ACCESS_TOKEN_IS_EMPTY(4017, "인증토큰을 확인할 수 없습니다.."),
    ACCESS_TOKEN_IS_DENIED(4018, "잘못된 인증토큰입니다."),
    REFRESH_TOKEN_IS_EMPTY(4019, "리프레쉬 인증토큰을 확인할 수 업습니다."),
    REFRESH_TOKEN_IS_EXPIRED(4020, "리프레쉬 인증토큰이 만료되었습니다."),
    REFRESH_TOKEN_IS_DENIED(4021, "잘못된 리프레쉬 인증토큰 입니다."),

    CLIENT_ID_IS_EMPTY(4022,"클라이언 아이디를 확인할 수 없습니다."),
    CLIENT_ID_IS_INVALID(4023,"잘못된 클라이언트 아이디입니다."),
    MUST_NEED_TOKEN_AND_ID(4024,"요청에 토큰과 클라이언트 아이디를 찾을수 없습니다."),
    //내부 api
    FRM_WRONG_REQUEST(5010,"FRM 서비스로의 요청이 잘못되었습니다."),
    FRM_SERVER_PROBLEM(5011,"frm-service 에 문제가 있습니다."),
    TRADE_WRONG_REQUEST(5020,"trade 서비스로의 요청이 잘못되었습니다."),
    TRADE_SERVER_PROBLEM(5021,"trade-service 에 문제가 있습니다."),

    ADDRESS_WRONG_REQUEST(5020,"address 서비스로의 요청이 잘못되었습니다."),
    ADDRESS_SERVER_PROBLEM(5021,"address-service 에 문제가 있습니다.")
    ;
    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
