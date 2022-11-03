package com.bmarket.securityservice;


import com.bmarket.securityservice.api.account.entity.Account;

import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SecurityServiceApplication {

    private final AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);


    }

    @PostConstruct
    public void setPostData() {
        initAdmin();
    }

    public void initAdmin() {

        if (!accountRepository.findByLoginId("manager").isPresent()) {
            Account account =new Account("manager","최고관리자","0000","manager@manager.com","070-070-0707");

            Account admin = accountRepository.save(account);

            log.info("[MANAGER ID={}]", admin.getId());
        }

    }

}
