package com.bmarket.securityservice.api.account.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.profile.service.ProfileCommandService;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;

import com.bmarket.securityservice.api.profile.entity.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
/**
 *  계정 서비스 로직중 command 에 해당 하는 로직 Transactional 안에서 변경이 일어나야 한다.
 *  create
 *  delete
 *  update
 */
public class AccountCommandService {

    private final AccountRepository accountRepository;
    private final ProfileCommandService profileCommandService;
    private final PasswordEncoder passwordEncoder;

    public SignupResult signUpProcessing(RequestSignUpForm form) {

        Profile profile = profileCommandService.createProfile(form);

        Account account = Account.createAccount()
                .loginId(form.getLoginId())
                .name(form.getName())
                .password(passwordEncoder.encode(form.getPassword()))
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);

        return new SignupResult(savedAccount.getClientId(), savedAccount.getCreatedAt());
    }


}
