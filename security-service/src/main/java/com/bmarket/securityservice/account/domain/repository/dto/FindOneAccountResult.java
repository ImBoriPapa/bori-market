package com.bmarket.securityservice.account.domain.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindOneAccountResult {
    private Long accountId;
    private String memberId;
    private String email;
    private String name;
    private String contact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;
    private LocalDateTime lastLogoutAt;

    @QueryProjection
    public FindOneAccountResult(Long accountId, String memberId, String email, String name, String contact, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLoginAt, LocalDateTime lastLogoutAt) {
        this.accountId = accountId;
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.lastLogoutAt = lastLogoutAt;
    }
}
