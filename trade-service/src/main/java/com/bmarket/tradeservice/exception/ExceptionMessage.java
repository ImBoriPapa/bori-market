package com.bmarket.tradeservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    NOTFOUND_TRADE("판매글을 찾을수 없습니다,"),
    ERROR_4XX_TO_FRM("frm 서버로 잘못된 요청을 보냈습니다."),
    ERROR_5XX_TO_FRM("frm 서버에 문제가 생겼습니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
