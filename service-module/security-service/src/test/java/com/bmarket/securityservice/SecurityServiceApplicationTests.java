package com.bmarket.securityservice;

import com.bmarket.securityservice.api.account.service.AccountCommandService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class SecurityServiceApplicationTests {

    private final AccountCommandService accountCommandService;

    @Test
    void contextLoads() {
    }


}
