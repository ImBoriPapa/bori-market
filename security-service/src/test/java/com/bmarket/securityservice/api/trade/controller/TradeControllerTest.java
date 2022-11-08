package com.bmarket.securityservice.api.trade.controller;

import com.bmarket.securityservice.api.account.controller.RequestAccountForm;
import com.bmarket.securityservice.api.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.security.controller.LoginResult;
import com.bmarket.securityservice.api.security.service.LoginService;
import com.bmarket.securityservice.api.trade.controller.RequestForm.RequestCreateTradeForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.bmarket.securityservice.utils.jwt.JwtHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.utils.jwt.JwtHeader.REFRESH_HEADER;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class TradeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    LoginService loginService;
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("moo")
                .name("momo")
                .password("momo123")
                .email("momo@ommo.com")
                .contact("010-2323-1133")
                .addressCode(1004)
                .city("서울시")
                .district("종로구")
                .town("평창동").build();
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
    }
    @AfterEach
    void AfterEach(){
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("거래 생성 테스트")
    void createTradeTest() throws Exception {
        //given
        LoginResult loginResult = loginService.loginProcessing("moo", "momo123");

        String token = loginResult.getToken();
        String refreshToken = loginResult.getRefreshToken();

        RequestCreateTradeForm tradeForm = new RequestCreateTradeForm();
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "jpg", "fsafafas".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "jpg", "fsafafas".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "image3.jpg", "jpg", "fsafafas".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "image4.jpg", "jpg", "fsafafas".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "image5.jpg", "jpg", "fsafafas".getBytes());

        String content = objectMapper.writeValueAsString(tradeForm);
        MockMultipartFile contentValue = new MockMultipartFile("form", "form", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/trade")
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .file(file4)
                        .file(file5)
                        .file(contentValue)
                        .header(AUTHORIZATION_HEADER, token)
                        .header(REFRESH_HEADER, refreshToken))
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("result").exists());
        //when

        //then

    }

    @Test
    @DisplayName("거래 내역 전체 조회 테스트")
    void searchSaleListTest() throws Exception {
        //given
        LoginResult loginResult = loginService.loginProcessing("moo", "momo123");
        String token = loginResult.getToken();
        String refreshToken = loginResult.getRefreshToken();
        mockMvc.perform(get("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token)
                        .header(REFRESH_HEADER, refreshToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists());


        //when

        //then

    }
    @Test
    @DisplayName("거래 상세 보기 테스트")
    void tradeDetailTest() throws Exception{
        //given
        LoginResult loginResult = loginService.loginProcessing("moo", "momo123");
        String token = loginResult.getToken();
        String refreshToken = loginResult.getRefreshToken();
        //when
        String expect = "";
        mockMvc.perform(get("/trade")
                        .header(AUTHORIZATION_HEADER,token)
                        .header(REFRESH_HEADER,refreshToken)
                .queryParam("tradeId", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").exists());
        //then

    }
}