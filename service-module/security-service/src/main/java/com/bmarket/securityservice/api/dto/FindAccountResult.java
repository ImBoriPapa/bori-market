package com.bmarket.securityservice.api.dto;

import com.bmarket.securityservice.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindAccountResult extends ResultForm {

    private String clientId;
    private String loginId;
    private String name;
    private String nickname;
    private String email;
    private String contact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FindAccountResult(Account account) {
        this.clientId = account.getClientId();
        this.loginId = account.getLoginId();
        this.name = account.getName();
        this.nickname = account.getProfile().getNickname();
        this.email = account.getProfile().getEmail();
        this.contact = account.getProfile().getContact();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
