package com.bmarket.securityservice.domain.account.repository.dto;

import com.bmarket.securityservice.domain.account.entity.Authority;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AccountList {

    private Long accountId;
    private String loginId;
    private String email;
    private Authority authority;
    private LocalDateTime createdAt;

    @QueryProjection
    public AccountList(Long accountId, String loginId, String email, Authority authority, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.email = email;
        this.authority = authority;
        this.createdAt = createdAt;
    }
}
