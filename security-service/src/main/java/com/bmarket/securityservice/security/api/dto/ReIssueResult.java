package com.bmarket.securityservice.security.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReIssueResult {
    private String memberId;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiredAt;

}
