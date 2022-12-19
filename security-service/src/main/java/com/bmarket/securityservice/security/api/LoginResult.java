package com.bmarket.securityservice.security.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Builder
public class LoginResult {
    private String memberId;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiredAt;
    private LocalDateTime loginAt;

    public LoginResult(String memberId, String accessToken, String refreshToken, Date accessTokenExpiredAt, LocalDateTime loginAt) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.loginAt = loginAt;
    }

}
