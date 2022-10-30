package com.bmarket.securityservice.api.account.service;

import com.bmarket.securityservice.api.address.AddressResult;

import com.bmarket.securityservice.api.account.controller.resultForm.FindAccountResult;
import com.bmarket.securityservice.api.account.controller.resultForm.AccountListResult;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;

import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;


    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher publisher;

    /**
     * address-service 모듈에서 addressCode 로 주소를 찾음
     *
     * @param addressCode
     * @return
     */
    private static AddressResult getAddress(int addressCode) {
        Flux<AddressResult> addressResultFlux = WebClient.create()
                .get()
                .uri("http://localhost:8085/addressData/address/" + addressCode)
                .retrieve()
                .bodyToFlux(AddressResult.class);

        return addressResultFlux.blockFirst();
    }

    /**
     * client id 로 계정 단건 조회
     *
     * @param clientId
     */
    public FindAccountResult findAccountByClientId(String clientId) {
        log.info("==============[AccountService] findByClientId  =============");
        Account account = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));
        return new FindAccountResult(account);
    }

    /**
     * account id 로 계정 단건 조회
     *
     * @param id
     */
    public Account findAccountByAccountId(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    /**
     * Pageable 을 사용한 페이징
     *
     * @param pageable
     * @return
     */
    public AccountListResult findAllAccount(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return new AccountListResult(accounts);
    }

    /**
     * Authority 변경
     *
     * @param id
     * @param authority
     */
    public void updateAuthority(Long id, Authority authority) {
        accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾지 못했습니다."))
                .updateAuthority(authority);
    }

    public void deleteAccount() {
        accountRepository.deleteAll();
    }

    public void deleteOneAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
