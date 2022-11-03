package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


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
                .from(account)
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

    @Override
    public Page<AccountList> findAccountListByPageable(Pageable pageable, Authority authority) {
        List<AccountList> result = queryFactory
                .select(new QAccountList(
                        account.id,
                        account.loginId,
                        account.email,
                        account.authority,
                        account.createdAt
                ))
                .from(account)
                .where(authorityEq(authority))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.id.desc())
                .fetch();

        Long totalCount = queryFactory
                .select(account.count())
                .from(account)
                .where(authorityEq(authority))
                .orderBy(account.id.desc())
                .fetchOne();
        return PageableExecutionUtils.getPage(result, pageable, () -> totalCount);

    }
    @Transactional(readOnly = true)
    public List<Account> useTransaction() {
        return queryFactory
                .selectFrom(account)
                .from(account)
                .fetch();
    }

    public List<Account> donUseTransaction() {
        return queryFactory
                .selectFrom(account)
                .from(account)
                .fetch();
    }

    public BooleanExpression authorityEq(Authority authority) {
        return authority != null ? account.authority.eq(authority) : null;
    }

}
