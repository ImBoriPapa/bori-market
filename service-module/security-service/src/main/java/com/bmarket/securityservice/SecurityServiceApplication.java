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
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }



}
