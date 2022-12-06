package com.bmarket.securityservice.domain.account.controller;

import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static com.bmarket.securityservice.utils.status.ResponseStatus.SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("local")
class AccountControllerSearchTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestDataProvider testDataProvider;

    @BeforeEach
    void beforeEach() {
        log.info("[=========================TEST DATA INIT START=============================]");
        testDataProvider.initAccountList(100);
        log.info("[=========================TEST DATA INIT FINISH=============================]");
    }

    @AfterEach
    void afterEach() {
        log.info("[=========================TEST DATA DELETE START=============================]");
        testDataProvider.clearAccount();
    }

    /**
     * 조건1: admin 계정 3개, 일반 계정 100개
     * 조건2: 검색조건 x default: page=0,size=20,Authority = null accountId DESC
     * expect response : page=0, size=20, totalCount=101 , 이전 페이지 URL x, 다음 페이지 URL o
     */
    @Test
    @DisplayName("계정 리스트 검색 조건없이 조회")
    void getAccountListTest1() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        mockMvc.perform(get("/account")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(0))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(103))
                .andExpect(jsonPath("result.accountLists").isArray())
                .andExpect(jsonPath("result.accountLists[0].accountId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].loginId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].email").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].authority").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("result.links[0].rel").exists())
                .andExpect(jsonPath("result.links[0].href").exists())
                .andDo(print());
    }

    /**
     * 조건1: admin 계정 3개, 일반 계정 100개
     * 조건2: 검색조건 page=3, size=20 Authority = null
     * expect response : page=3, size=20, totalCount=101 , 이전 페이지 URL o, 다음 페이지 URL x
     */
    @Test
    @DisplayName("계정 리스트 검색 마지막 페이지")
    void getAccountListTest2() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        mockMvc.perform(get("/account?page=5")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(5))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(103))
                .andExpect(jsonPath("result.accountLists").isArray())
                .andExpect(jsonPath("result.accountLists[0].accountId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].loginId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].email").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].authority").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("result.links[0].rel").exists())
                .andExpect(jsonPath("result.links[0].href").exists())
                .andDo(print());
    }

    /**
     * 조건1: admin 계정 3개, 일반 계정 100개
     * 조건2: 검색조건 page=5, size=20 Authority = null
     * expect response : page=0, size=20, totalCount=101 , 이전 페이지 URL o, 다음 페이지 URL o
     */
    @Test
    @DisplayName("계정 리스트 검색 마지막 페이지")
    void getAccountListTest3() throws Exception {
        //given

        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        mockMvc.perform(get("/account?page=3")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(3))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(103))
                .andExpect(jsonPath("result.accountLists").isArray())
                .andExpect(jsonPath("result.accountLists[0].accountId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].loginId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].email").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].authority").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("result.links[0].rel").exists())
                .andExpect(jsonPath("result.links[0].href").exists())
                .andExpect(jsonPath("result.links[1].href").exists())
                .andExpect(jsonPath("result.links[1].href").exists())
                .andDo(print());
    }

    /**
     * 조건1: admin 계정 2개, 일반 계정 100개
     * 조건2: 검색조건 page=0, size=20 Authority = ADMIN
     * expect response : page=3, size=20, totalCount=101 , 이전 페이지 URL x, 다음 페이지 URL x
     */
    @Test
    @DisplayName("계정 리스트 Authority 검색 ")
    void getAccountListTest4() throws Exception {
        //given

        LoginResult loginResult = jwtService.loginProcessing("tester1", "!@tester1234");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        mockMvc.perform(get("/account?authority=ADMIN")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(0))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(2))
                .andExpect(jsonPath("result.accountLists").isArray())
                .andExpect(jsonPath("result.accountLists[0].accountId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].loginId").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].email").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].authority").isNotEmpty())
                .andExpect(jsonPath("result.accountLists[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("result.links[0].rel").exists())
                .andExpect(jsonPath("result.links[0].href").exists())
                .andDo(print());
    }
}