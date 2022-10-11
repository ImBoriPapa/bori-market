package com.bmarket.securityservice.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LoginResult {

    private String clientId;
    private String token;
    private String refreshToken;
    private LocalDateTime loginAt;

    public LoginResult(String clientId, String token, String refreshToken, LocalDateTime loginAt) {
        this.clientId = clientId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.loginAt = loginAt;
    }
}
