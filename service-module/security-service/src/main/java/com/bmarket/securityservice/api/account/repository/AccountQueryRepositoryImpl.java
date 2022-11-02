package com.bmarket.securityservice.api.account.repository;


import com.bmarket.securityservice.api.account.repository.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static com.bmarket.securityservice.api.account.entity.QAccount.*;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * security loadUserByUsername()에 사용할 clientId,password,authority 반환
     *
     * @param clientId
     * @return
     */
    public Optional<InfoForLoadByUsername> findAccountForLoadUser(String clientId) {
        InfoForLoadByUsername result = queryFactory
                .select(new QInfoForLoadByUsername(
                        account.clientId,
                        account.password,
                        account.authority))
                .where(account.clientId.eq(clientId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 계정 단건 조회
     *
     * @param accountId
     * @return
     */
    @Override
    public Optional<FindOneAccountResult> findOneAccount(Long accountId) {

        FindOneAccountResult result = queryFactory
                .select(new QFindOneAccountResult(
                        account.id,
                        account.loginId,
                        account.name,
                        account.email,
                        account.contact,
                        account.createdAt,
                        account.updatedAt))
                .from(account)
                .where(account.id.eq(accountId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public AccountListResult findAccountListByPageable(int offSet, int size) {
        List<AccountList> result = queryFactory
                .select(new QAccountList(
                        account.id,
                        account.loginId,
                        account.email,
                        account.authority,
                        account.createdAt
                ))
                .from(account)
                .offset(offSet)
                .limit(size)
                .orderBy(account.id.desc())
                .fetch();

        return new AccountListResult(offSet, result.size(), result);
    }

}
