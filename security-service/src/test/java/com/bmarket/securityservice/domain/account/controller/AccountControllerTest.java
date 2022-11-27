package com.bmarket.securityservice.domain.account.controller;

import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.domain.security.controller.LoginController;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import com.bmarket.securityservice.utils.LinkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static com.bmarket.securityservice.utils.status.ResponseStatus.SUCCESS;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
class AccountControllerTest {
    @Autowired
    AccountController accountController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountCommandService accountCommandService;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LinkProvider linkProvider;
    @Autowired
    JwtService jwtService;

    @Autowired
    TestDataProvider testDataProvider;
    public MockWebServer mockWebServer;


    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/profile/default");

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody("http://localhost:8095/frm/profile/default.jpg");

        mockWebServer.enqueue(mockResponse);
        log.info("[Test Data Init Start]============================================");
        testDataProvider.initAccountList(10);
        log.info("[Test Data Init Finish]============================================");


    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
        log.info("[Test Data Delete Start]============================================");
        testDataProvider.clearAccount();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successSignupTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("loginId")
                .name("로그인")
                .password("login123!@#")
                .nickname("nickname")
                .email("login@login.com")
                .contact("01022321313")
                .addressCode(1001)
                .city("서울")
                .district("강남구")
                .town("대치동")
                .build();
        String value = objectMapper.writeValueAsString(form);
        Link loginLink = linkProvider.createOneLink(LoginController.class, "login", "POST : 로그인");

        //when
        mockMvc.perform(post("/account")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))

                //then
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value(SUCCESS.toString()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("$.result.accountId").isNotEmpty())
                .andExpect(jsonPath("$.result.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.result.links").isNotEmpty())
                .andExpect(jsonPath("$.result.links[0].rel").value(loginLink.getRel().toString()))
                .andExpect(jsonPath("$.result.links[0].href").value(loginLink.getHref()))
                .andDo(print());
    }

    @Test
    @DisplayName("계정 단건 조회 테스트")
    void getAccountTest() throws Exception {
        //given
        /**
         * Test Data
         * loginId = tester1
         * name = 테스터1
         * password = !@tester1234
         * email = test1@test.com
         * contact = 01011111
         * nickname = test1
         * addressCode = 1001, city = 서울, district = 종로구, town = 암사동
         */
        //when
        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        mockMvc.perform(get("http://localhost:8080/account/{accountId}", loginResult.getAccountId())
                        .headers(headers)
                )
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.accountId").value(loginResult.getAccountId()))
                .andExpect(jsonPath("result.loginId").value("tester1"))
                .andExpect(jsonPath("result.name").value("테스터1"))
                .andExpect(jsonPath("result.email").value("test1@test.com"))
                .andExpect(jsonPath("result.contact").value("01011111"))
                .andExpect(jsonPath("result.createdAt").isNotEmpty())
                .andExpect(jsonPath("result.updatedAt").isNotEmpty())
                .andDo(print());
    }


    @Test
    @DisplayName("계정 삭제 테스트")
    void deleteAccountTest() throws Exception {
        //given
        /**
         * Test Data
         * loginId = tester1
         * name = 테스터1
         * password = !@tester1234
         * email = test1@test.com
         * contact = 01011111
         * nickname = test1
         * addressCode = 1001, city = 서울, district = 종로구, town = 암사동
         */
        //when
        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");

        RequestAccountForm.DeleteForm deleteForm = new RequestAccountForm.DeleteForm("!@tester1234");
        String value = objectMapper.writeValueAsString(deleteForm);
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        //then
        mockMvc.perform(delete("/account/" + loginResult.getAccountId())
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(SUCCESS.toString()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("계정 비밀번호 변경 테스트")
    void updatePasswordTest() throws Exception {
        //given
        /**
         * Test Data
         * loginId = tester1
         * name = 테스터1
         * password = !@tester1234
         * email = test1@test.com
         * contact = 01011111
         * nickname = test1
         * addressCode = 1001, city = 서울, district = 종로구, town = 암사동
         */
        //when
        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");
        RequestAccountForm.UpdateEmailForm emailForm = new RequestAccountForm.UpdateEmailForm("new@new.com");
        String value = objectMapper.writeValueAsString(emailForm);
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //then
        mockMvc.perform(put("/account/" + loginResult.getAccountId() + "/email")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andDo(print());

    }
}