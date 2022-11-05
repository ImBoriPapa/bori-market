package com.bmarket.securityservice.controller;

import com.bmarket.securityservice.api.account.controller.AccountController;
import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.security.controller.LoginController;
import com.bmarket.securityservice.api.security.controller.LoginResult;
import com.bmarket.securityservice.api.security.service.LoginService;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.bmarket.securityservice.utils.jwt.JwtHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.JwtHeader.REFRESH_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    LoginService loginService;

    @BeforeEach
    void beforeEach(){
        log.info("[BeforeEach]");
    }
    @AfterEach
    void afterEach(){
        log.info("[AfterEach]");
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successSignupTest() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
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
        String value = objectMapper.writeValueAsString(form);
        Link loginLink = WebMvcLinkBuilder.linkTo(LoginController.class).slash("login").withRel("POST : 로그인");
        //when
        mockMvc.perform(post("/account")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
        //then
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value(ResponseStatus.SUCCESS.toString()))
                .andExpect(jsonPath("code").value(ResponseStatus.SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.SUCCESS.getMessage()))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("$.result.accountId").isNotEmpty())
                .andExpect(jsonPath("$.result.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.result.links").isNotEmpty())
                .andExpect(jsonPath("$.result.links[0].rel").value(loginLink.getRel().toString()))
                .andExpect(jsonPath("$.result.links[0].href").value(loginLink.getHref()))
                .andExpect(jsonPath("$.result.links[1].rel").value("GET   : 계정 정보 조회"))
                .andExpect(jsonPath("$.result.links[1].href").isNotEmpty())
                .andExpect(jsonPath("$.result.links[2].rel").value("PATCH : 계정 정보 수정"))
                .andExpect(jsonPath("$.result.links[2].href").isNotEmpty())
                .andExpect(jsonPath("$.result.links[3].rel").value("DELETE: 계정 정보 삭제"))
                .andExpect(jsonPath("$.result.links[3].href").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("계정 단건 조회 테스트")
    void getAccountTest() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
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
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        Long accountId = signupResult.getAccountId();
        LoginResult loginResult = loginService.loginProcessing(form.getLoginId(), form.getPassword());

        mockMvc.perform(get("http://localhost:8080/account/{accountId}",loginResult.getClientId())
                        .header(AUTHORIZATION_HEADER,loginResult.getToken())
                        .header(REFRESH_HEADER,loginResult.getRefreshToken())
                )

        //then
                .andExpect(status().isOk())
                .andDo(print());



    }
}