package com.bmarket.securityservice.controller;

import com.bmarket.securityservice.domain.account.controller.AccountController;
import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.domain.security.controller.LoginController;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.SecurityHeader.REFRESH_HEADER;
import static com.bmarket.securityservice.utils.status.ResponseStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
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
    public MockWebServer mockWebServer;
    String defaultImage = "http://localhost:8095/frm/default.img";

    @BeforeEach
    void beforeEach() throws IOException {
        log.info("[BeforeEach]");
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/profile/default");
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(defaultImage);
        mockWebServer.enqueue(mockResponse);

//        ArrayList<ResponseAccountForm.ResponseSignupForm> results = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
//                    .loginId("loginId" + i)
//                    .name("로그인")
//                    .password("login123")
//                    .nickname("nickname" + i)
//                    .email("login" + i + "@login.com")
//                    .contact("010-" + i + "-1313")
//                    .addressCode(1001)
//                    .city("서울")
//                    .district("강남구")
//                    .town("대치동")
//                    .build();
//            ResponseAccountForm.ResponseSignupForm responseSignupForm = accountCommandService.signUpProcessing(form);
//            results.add(responseSignupForm);
//        }
    }

    @AfterEach
    void afterEach() throws IOException {
    mockWebServer.shutdown();
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
                .andExpect(jsonPath("$.result.links[1].rel").isNotEmpty())
                .andExpect(jsonPath("$.result.links[1].href").isNotEmpty())
                .andExpect(jsonPath("$.result.links[2].rel").isNotEmpty())
                .andExpect(jsonPath("$.result.links[2].href").isNotEmpty())
                .andExpect(jsonPath("$.result.links[3].rel").isNotEmpty())
                .andExpect(jsonPath("$.result.links[3].href").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("계정 단건 조회 테스트")
    void getAccountTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("loginId")
                .name("로그인")
                .password("login123")
                .nickname("nickname")
                .email("login@login.com")
                .contact("010-2232-1313")
                .addressCode(1001)
                .city("서울")
                .district("강남구")
                .town("대치동")
                .build();
        //when
        ResponseAccountForm.ResponseSignupForm responseSignupForm = accountCommandService.signUpProcessing(form);
        Long accountId = responseSignupForm.getAccountId();
        LoginResult loginResult = jwtService.loginProcessing(form.getLoginId(), form.getPassword());
        mockMvc.perform(get("http://localhost:8080/account/{accountId}", accountId)
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                )

                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.accountId").value(accountId))
                .andExpect(jsonPath("result.loginId").value(form.getLoginId()))
                .andExpect(jsonPath("result.name").value(form.getName()))
                .andExpect(jsonPath("result.email").value(form.getEmail()))
                .andExpect(jsonPath("result.contact").value(form.getContact()))
                .andExpect(jsonPath("result.createdAt").isNotEmpty())
                .andExpect(jsonPath("result.updatedAt").isNotEmpty())
                .andDo(print());

    }

    /**
     * 조건1: admin 계정 1개, 일반 계정 100개
     * 조건2: 검색조건 x default: page=0,size=20,Authority = null accountId DESC
     * expect response : page=0, size=20, totalCount=101 , 이전 페이지 URL x, 다음 페이지 URL o
     */
    @Test
    @DisplayName("계정 리스트 검색 조건없이 조회")
    void getAccountListTest1() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("loginId1", "login123");
        //when
        mockMvc.perform(get("/account")
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(0))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(101))
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
     * 조건1: admin 계정 1개, 일반 계정 100개
     * 조건2: 검색조건 page=3, size=20 Authority = null
     * expect response : page=3, size=20, totalCount=101 , 이전 페이지 URL o, 다음 페이지 URL x
     */
    @Test
    @DisplayName("계정 리스트 검색 마지막 페이지")
    void getAccountListTest2() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("loginId1", "login123");
        //when
        mockMvc.perform(get("/account?page=5")
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(5))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(101))
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
     * 조건1: admin 계정 1개, 일반 계정 100개
     * 조건2: 검색조건 page=5, size=20 Authority = null
     * expect response : page=0, size=20, totalCount=101 , 이전 페이지 URL o, 다음 페이지 URL o
     */
    @Test
    @DisplayName("계정 리스트 검색 마지막 페이지")
    void getAccountListTest3() throws Exception {
        //given

        LoginResult loginResult = jwtService.loginProcessing("loginId1", "login123");
        //when
        mockMvc.perform(get("/account?page=3")
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(3))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(101))
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
     * 조건1: admin 계정 1개, 일반 계정 100개
     * 조건2: 검색조건 page=0, size=20 Authority = ADMIN
     * expect response : page=3, size=20, totalCount=101 , 이전 페이지 URL x, 다음 페이지 URL x
     */
    @Test
    @DisplayName("계정 리스트 Authority 검색 ")
    void getAccountListTest4() throws Exception {
        //given

        LoginResult loginResult = jwtService.loginProcessing("loginId1", "login123");
        //when
        mockMvc.perform(get("/account?authority=ADMIN")
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(SUCCESS.name()))
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(SUCCESS.getMessage()))
                .andExpect(jsonPath("result.pageNumber").value(0))
                .andExpect(jsonPath("result.size").value(20))
                .andExpect(jsonPath("result.totalCount").value(1))
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

    @Test
    @DisplayName("계정 삭제 테스트")
    void deleteAccountTest() throws Exception {
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
        ResponseAccountForm.ResponseSignupForm signupResult = accountCommandService.signUpProcessing(form);
        LoginResult loginResult = jwtService.loginProcessing(form.getLoginId(), form.getPassword());

        RequestAccountForm.DeleteForm deleteForm = new RequestAccountForm.DeleteForm(form.getPassword());
        String value = objectMapper.writeValueAsString(deleteForm);
        //when
        mockMvc.perform(delete("/account/" + signupResult.getAccountId())
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))

                //then
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
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
        LoginResult loginResult = jwtService.loginProcessing("loginId1", "login123");
        RequestAccountForm.UpdateEmailForm emailForm = new RequestAccountForm.UpdateEmailForm("new@new.com");
        String value = objectMapper.writeValueAsString(emailForm);
        //when
        mockMvc.perform(put("/account/" + loginResult.getAccountId() + "/email")
                        .header(AUTHORIZATION_HEADER, loginResult.getAccessToken())
                        .header(REFRESH_HEADER, loginResult.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                //then
                .andExpect(status().isOk())
                .andDo(print());

    }
}