package com.bmarket.securityservice.api.account.service;

import com.bmarket.securityservice.api.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountQueryRepository;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService {

    private final AccountRepository accountRepository;
    private final AccountQueryRepository queryRepository;

    /**
     * account id 로 계정 단건 조회
     * @param accountId
     */
    public FindOneAccountResult findAccountByClientId(Long accountId) {
         return queryRepository.findOneAccount(accountId)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    /**
     * account id 로 계정 단건 조회
     * @param id
     */
    public Account findAccountByAccountId(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    /**
     * Pageable 을 사용한 페이징
     * @param pageable
     * @return
     */
    public AccountListResult findAllAccount(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return null;
    }



}
