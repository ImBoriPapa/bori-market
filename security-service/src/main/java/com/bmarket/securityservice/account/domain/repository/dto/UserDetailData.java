package com.bmarket.securityservice.account.domain.repository.dto;

import com.bmarket.securityservice.account.domain.entity.Authority;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  Security 인증 객체를 만들기 위한 정보를 Account 객체에서 받아 오기 위한 dto
 */
@Getter
@NoArgsConstructor
public class UserDetailData {
    private String memberId;
    private String password;
    private Authority authority;

    @QueryProjection
    public UserDetailData(String memberId, String password, Authority authority) {
        this.memberId = memberId;
        this.password = password;
        this.authority = authority;
    }
}
