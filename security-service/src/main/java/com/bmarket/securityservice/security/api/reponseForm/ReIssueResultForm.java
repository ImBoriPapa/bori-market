package com.bmarket.securityservice.security.api.reponseForm;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ReIssueResultForm {

    private String memberId;
    private Date accessTokenExpiredAt;

    public ReIssueResultForm(String memberId, Date accessTokenExpiredAt) {
        this.memberId = memberId;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
    }
}
