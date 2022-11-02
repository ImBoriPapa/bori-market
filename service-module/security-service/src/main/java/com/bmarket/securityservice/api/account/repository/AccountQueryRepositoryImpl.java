package com.bmarket.securityservice.api.account.repository;


import com.bmarket.securityservice.api.account.repository.dto.InfoForLoadByUsername;
import com.bmarket.securityservice.api.account.repository.dto.QInfoForLoadByUsername;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.bmarket.securityservice.api.account.entity.QAccount.*;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<InfoForLoadByUsername> findAccountForLoadUser(String clientId) {
        InfoForLoadByUsername result = queryFactory
                .select(new QInfoForLoadByUsername(
                        account.clientId,
                        account.password,
                        account.authority))
                .where(account.clientId.eq(clientId))
                .fetchOne();
        return Optional.of(result);
    }

}
