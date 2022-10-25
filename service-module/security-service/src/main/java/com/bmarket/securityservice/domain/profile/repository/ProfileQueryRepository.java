package com.bmarket.securityservice.domain.profile.repository;


import com.bmarket.securityservice.api.controller.profile.response.QResponseProfileResult;
import com.bmarket.securityservice.api.controller.profile.response.ResponseProfileResult;
import com.bmarket.securityservice.domain.account.entity.QAccount;
import com.bmarket.securityservice.domain.profile.entity.QProfile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileQueryRepository {

    private final JPAQueryFactory queryFactory;

    public void getProfile(String clientId) {
        List<ResponseProfileResult> results = queryFactory.select(new QResponseProfileResult(
                        QProfile.profile.nickname,
                        QProfile.profile.email,
                        QProfile.profile.contact,
                        QProfile.profile.profileImage,
                        QProfile.profile.contact,
                        QProfile.profile.addressRange.stringValue()
                ))
                .from(QAccount.account.profile)
                .where(QAccount.account.clientId.eq(clientId))
                .fetch();

    }
}
