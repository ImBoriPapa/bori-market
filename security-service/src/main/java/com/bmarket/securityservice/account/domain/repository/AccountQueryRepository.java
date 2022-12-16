package com.bmarket.securityservice.account.domain.repository;

import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.dto.AccountList;
import com.bmarket.securityservice.account.domain.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.account.domain.repository.dto.UserDetailData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountQueryRepository {
    Optional<UserDetailData> findAccountForLoadUser(String memberId);
    Optional<FindOneAccountResult> findOneAccount(Long accountId);

    Page<AccountList> findAccountListByPageable(Pageable pageable, Authority authority);
}
