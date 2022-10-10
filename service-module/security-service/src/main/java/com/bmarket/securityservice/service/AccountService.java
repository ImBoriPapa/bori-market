package com.bmarket.securityservice.service;

import com.bmarket.securityservice.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.dto.FindAccountResult;
import com.bmarket.securityservice.dto.AccountListResult;
import com.bmarket.securityservice.dto.SignupResult;
import com.bmarket.securityservice.entity.Account;
import com.bmarket.securityservice.entity.Authority;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 계정 생성
     * ->Account.createAccount() 로 생성
     * ->패스워드 인코딩
     * ->생성시 Authority 는 ROLL_USER 설정
     *
     * @param form
     * @return
     */
    public SignupResult signUpProcessing(RequestSignUpForm form) {

        Account account = Account.createAccount()
                .loginId(form.getLoginId())
                .name(form.getName())
                .nickname(form.getNickname())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .contact(form.getContact())
                .authority(Authority.ROLL_USER).build();

        Account save = accountRepository.save(account);

        return new SignupResult(save.getId(), save.getClientId(), save.getCreatedAt());
    }

    /**
     * client id 로 계정 단건 조회
     *
     * @param clientId
     */
    public FindAccountResult findAccountByClientId(String clientId) {
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
