package com.bmarket.securityservice.api.account.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FindOneAccountResult extends EntityModel {
    private Long accountId;
    private String loginId;
    private String name;
    private String email;
    private String contact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public FindOneAccountResult(Long accountId, String loginId, String name , String email, String contact, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.accountId = accountId;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
