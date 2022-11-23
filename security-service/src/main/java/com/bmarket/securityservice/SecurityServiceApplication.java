package com.bmarket.securityservice;


import com.bmarket.securityservice.utils.testdata.TestDataProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SecurityServiceApplication {

    public final TestDataProvider testDataProvider;

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

}
