package com.bmarket.securityservice;


import com.bmarket.securityservice.domain.testdata.TestData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceApplication {

    public final TestData testData;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

}
