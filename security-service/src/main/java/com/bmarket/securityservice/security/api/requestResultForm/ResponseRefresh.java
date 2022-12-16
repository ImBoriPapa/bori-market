package com.bmarket.securityservice.security.api.requestResultForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRefresh {
    private String accessToken;
    private String refreshToken;
}
