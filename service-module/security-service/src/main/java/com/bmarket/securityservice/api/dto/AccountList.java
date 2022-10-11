package com.bmarket.securityservice.api.dto;

import com.bmarket.securityservice.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountList {

    private String clientId;
    private String loginId;
    private String nickname;
    private String email;
    private String contact;

    public AccountList(Account account) {
        this.clientId = account.getClientId();
        this.loginId = account.getLoginId();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.contact = account.getContact();
    }
}
