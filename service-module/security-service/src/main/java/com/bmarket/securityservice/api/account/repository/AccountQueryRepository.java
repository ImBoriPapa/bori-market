package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.account.repository.dto.InfoForLoadByUsername;

import java.util.Optional;

public interface AccountQueryRepository {

    Optional<InfoForLoadByUsername> findAccountForLoadUser(String clientId);

    Optional<FindOneAccountResult> findOneAccount(Long accountId);
}
