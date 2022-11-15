package com.bmarket.securityservice;


import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.domain.account.entity.Account;

import com.bmarket.securityservice.domain.account.repository.AccountRepository;

import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SecurityServiceApplication {

    private final AccountRepository accountRepository;
    private final AccountCommandService accountCommandService;

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);


    }

    @PostConstruct
    public void setPostData() {
        initAdmin();
    }

    public void initAdmin() {

        if (!accountRepository.findByLoginId("manager").isPresent()) {
            String password = passwordEncoder.encode("0000");
            Account account = new Account("manager", "최고관리자", password, "manager@manager.com", "070-070-0707");

            Account admin = accountRepository.save(account);

            log.info("[MANAGER ID={}]", admin.getId());
        }

    }
    public void initTestAccount() {
        ArrayList<ResponseAccountForm.ResponseSignupForm> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                    .loginId("loginId" + i)
                    .name("로그인")
                    .password("login123")
                    .nickname("nickname" + i)
                    .email("login" + i + "@login.com")
                    .contact("010-" + i + "-1313")
                    .addressCode(1001)
                    .city("서울")
                    .district("강남구")
                    .town("대치동")
                    .build();
            ResponseAccountForm.ResponseSignupForm responseSignupForm = accountCommandService.signUpProcessing(form);
            results.add(responseSignupForm);
        }
    }
}
