package com.bmarket.securityservice.account.domain.repository;

import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.bmarket.securityservice.account.domain.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDetailData> findAccountForLoadUser(String memberId) {
        UserDetailData result = queryFactory
                .select(new QUserDetailData(
                        account.memberId,
                        account.password,
                        account.authority))
                .from(account)
                .where(account.memberId.eq(memberId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<FindOneAccountResult> findOneAccount(Long accountId) {
        FindOneAccountResult result = queryFactory
                .select(new QFindOneAccountResult(
                        account.id,
                        account.memberId,
                        account.email,
                        account.name,
                        account.contact,
                        account.createdAt,
                        account.updatedAt,
                        account.lastLoginTime,
                        account.lastLogoutTime))
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


    public BooleanExpression authorityEq(Authority authority) {
        return authority != null ? account.authority.eq(authority) : null;
    }
}
