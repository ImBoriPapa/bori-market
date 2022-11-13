package com.bmarket.securityservice.utils.status;

import lombok.Getter;

@Getter
public enum AuthenticationFilterStatus {
    FILTER_STATUS,
    CLIENT_ID_EMPTY,
    TOKEN_IS_EMPTY,
    TOKEN_IS_DENIED,
    REFRESH_TOKEN_IS_EMPTY,
    REFRESH_TOKEN_IS_EXPIRED,
    REFRESH_TOKEN_IS_DENIED

}
