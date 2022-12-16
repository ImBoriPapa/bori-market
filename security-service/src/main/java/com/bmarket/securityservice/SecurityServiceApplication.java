package com.bmarket.securityservice;


import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceApplication {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @PostConstruct
    public void post() {
        init();
    }

    public void init() {
        Account account = Account.createAccount()
                .email("email@email.com")
                .name("test")
                .password(passwordEncoder.encode("!@test1234"))
                .authority(Authority.USER)
                .contact("").build();
        accountRepository.save(account);
    }
}
