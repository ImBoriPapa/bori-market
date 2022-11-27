package com.bmarket.securityservice;


import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }
}
