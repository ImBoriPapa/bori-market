package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.api.account.controller.RequestAccountForm;
import com.bmarket.securityservice.api.security.controller.LoginResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountQueryService;
import com.bmarket.securityservice.api.security.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.SecurityHeader.REFRESH_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class CustomAccessDeniedHandlerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountQueryService accountQueryService;


    @Autowired
    AccountRepository accountRepository;
    @Autowired
    JwtService jwtService;

    @BeforeEach
    void before() {
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("happy")
                .name("happyHappy")
                .password("happy123")
                .email("happy@happy.com")
                .nickname("happyMan")
                .contact("010-2323-1341")
                .build();

    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("권한이 없는 접근 테스트")
    void noPower() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("happy", "happy123");
        Optional<Account> account = accountRepository.findByClientId(login.getClientId());
        log.info("Account Authority={} ",account.get().getAuthorityList());

        mockMvc.perform(get("/jwt-test2")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, login.getAccessToken())
                .header(REFRESH_HEADER, login.getRefreshToken()));

        //when

        //then

    }
}