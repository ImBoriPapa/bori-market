package com.bmarket.securityservice.api.security.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LoginResult {
    private Long accountId;
    private String clientId;
    private String token;
    private String refreshToken;
    private LocalDateTime loginAt;

    public LoginResult(Long accountId,String clientId, String token, String refreshToken, LocalDateTime loginAt) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.loginAt = loginAt;
    }
}
