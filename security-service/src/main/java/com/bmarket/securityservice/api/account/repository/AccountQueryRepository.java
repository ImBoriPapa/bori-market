package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.AccountList;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.account.repository.dto.UserDetailData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountQueryRepository {

    Optional<UserDetailData> findAccountForLoadUser(String clientId);

    Optional<FindOneAccountResult> findOneAccount(Long accountId);

    Page<AccountList> findAccountListByPageable(Pageable pageable, Authority authority);

    List<Account> useTransaction();
    List<Account> donUseTransaction();
}
