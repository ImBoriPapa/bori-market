package com.bmarket.securityservice.domain.account.repository;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.dto.AccountList;
import com.bmarket.securityservice.domain.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.domain.account.repository.dto.UserDetailData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountQueryRepository {

    Optional<UserDetailData> findAccountForLoadUser(Long accountId);
    Optional<FindOneAccountResult> findOneAccount(Long accountId);

    Page<AccountList> findAccountListByPageable(Pageable pageable, Authority authority);

    List<Account> useTransaction();
    List<Account> doNotUseTransaction();
}
