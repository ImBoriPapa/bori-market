package com.bmarket.securityservice.api.account.controller.resultForm;

import com.bmarket.securityservice.api.account.entity.Account;
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
        this.nickname = account.getProfile().getNickname();
        this.email = account.getProfile().getEmail();
        this.contact = account.getProfile().getContact();
    }
}
