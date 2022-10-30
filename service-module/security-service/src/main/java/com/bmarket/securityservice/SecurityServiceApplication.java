package com.bmarket.securityservice;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.trade.service.RequestTradeApi;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class SecurityServiceApplication {

    private final AccountCommandService accountCommandService;
    private final RequestTradeApi requestTradeApi;

    private final AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @PostConstruct
    public void postCon() {

    }
    public void init() {

        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("moo")
                .name("momo")
                .password("momo123")
                .email("momo@ommo.com")
                .contact("010-2323-1133")
                .addressCode(1004)
                .city("서울시")
                .district("종로구")
                .town("평창동").build();
        SignupResult result = accountCommandService.signUpProcessing(form);
            }

}
