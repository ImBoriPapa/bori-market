package com.bmarket.securityservice.security.constant;

import lombok.Getter;

@Getter
public enum AuthenticationFilterStatus {

    FILTER_STATUS("",""),
    CLIENT_ID_STATUS("",""),
    CLIENT_ID_EMPTY("/empty-clientId","/exception/empty-clientId"),
    CLIENT_ID_IS_INVALID("/wrong-clientId","/exception/wrong-clientId"),
    TOKEN_IS_EMPTY("/empty-token","/exception/empty-token?token=access"),
    TOKEN_IS_DENIED("/denied-token","/exception/denied-token?token=access"),
    REFRESH_TOKEN_IS_EMPTY("/empty-token","/exception/empty-token?token=refresh"),
    REFRESH_TOKEN_IS_EXPIRED("/expired-token","/exception/expired-token"),
    REFRESH_TOKEN_IS_DENIED("/denied-token","/exception/denied-token?token=refresh"),
    EMPTY_BOTH("/empty-both","/exception/empty-both");

    public final String ex;
    public final String url;

    AuthenticationFilterStatus(String ex, String url) {
        this.ex = ex;
        this.url = url;
    }
}
