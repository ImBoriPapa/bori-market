package com.bmarket.securityservice;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
class SecurityServiceApplicationTests {

    private final AccountCommandService accountCommandService;

    @Test
    void contextLoads() {
    }

}
