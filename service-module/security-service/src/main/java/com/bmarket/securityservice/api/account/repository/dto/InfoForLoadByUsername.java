package com.bmarket.securityservice.api.account.repository.dto;

import com.bmarket.securityservice.api.account.entity.Authority;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InfoForLoadByUsername {

    private String clientId;
    private String password;
    private Authority authority;

    @QueryProjection
    public InfoForLoadByUsername(String clientId, String password, Authority authority) {
        this.clientId = clientId;
        this.password = password;
        this.authority = authority;
    }
}
