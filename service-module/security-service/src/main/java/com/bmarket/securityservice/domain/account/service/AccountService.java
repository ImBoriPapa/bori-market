package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.dto.FindAccountResult;
import com.bmarket.securityservice.api.dto.AccountListResult;
import com.bmarket.securityservice.api.dto.SignupResult;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
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
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 계정 생성
     * ->Profile.createProfile() 프로필 정보 생성
     * ->Account.createAccount() 계정 정보 생성
     * ->패스워드 인코딩
     * @param  form -> RequestSignUpForm
     * @return SignupResult : {clientId,createdAt}
     */
    public SignupResult signUpProcessing(RequestSignUpForm form) {
        Profile profile = Profile.createProfile()
                .nickname(form.getNickname())
                .email(form.getEmail())
                .contact(form.getContact())
                .firstAddressCode(form.getAddressCode())
                .build();
        Profile savedProfile = profileRepository.save(profile);

        Account account = Account.createAccount()
                .loginId(form.getLoginId())
                .name(form.getName())
                .password(passwordEncoder.encode(form.getPassword()))
                .profile(savedProfile)
                .build();
        Account savedAccount = accountRepository.save(account);

        savedAccount.getProfile().initClientId(savedAccount.getClientId());

        return new SignupResult(savedAccount.getClientId(), savedAccount.getCreatedAt());
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
