package com.bmarket.securityservice.domain.address;

import lombok.Getter;

@Getter
public enum AddressRange {
    ONLY("현재 주소의 동네만 검색합니다."),
    FIVE("현재 주소 근처 5개의 동네를 포함합니다."),
    TEN("현재 주소 근처 10개의 동네를 포함합니다.");

    public String expression;

    AddressRange(String expression) {
        this.expression = expression;
    }
}
