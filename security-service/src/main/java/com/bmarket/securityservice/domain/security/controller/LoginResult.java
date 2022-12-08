package com.bmarket.securityservice.domain.security.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Builder
public class LoginResult {
    private Long accountId;
    private String accessToken;
    private Date accessTokenExpiredAt;
    private String refreshToken;
    private LocalDateTime loginAt;

    public LoginResult(Long accountId, String accessToken, Date accessTokenExpiredAt, String refreshToken, LocalDateTime loginAt) {
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshToken = refreshToken;
        this.loginAt = loginAt;
    }
}
