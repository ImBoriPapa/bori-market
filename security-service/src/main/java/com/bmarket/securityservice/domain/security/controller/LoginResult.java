package com.bmarket.securityservice.domain.security.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class LoginResult {
    private Long accountId;
    private String clientId;
    private String accessToken;
    private Date accessTokenExpiredAt;
    private String refreshToken;
    private LocalDateTime loginAt;

    public LoginResult(Long accountId, String clientId, String accessToken, Date accessTokenExpiredAt, String refreshToken, LocalDateTime loginAt) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshToken = refreshToken;
        this.loginAt = loginAt;
    }
}
