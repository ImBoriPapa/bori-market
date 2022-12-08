package com.bmarket.securityservice.domain.trade.controller;

import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("local")
class TradeControllerTest {

    @Autowired
    TestDataProvider testDataProvider;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach(){
        testDataProvider.initAccount();
    }
    @AfterEach
    void afterEach(){
        testDataProvider.clearAccount();
    }

    @Test
    @DisplayName("거래 생성")
    void 거래생성() throws Exception{
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();

        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        RequestTradeForm.CreateTradeForm form = RequestTradeForm.CreateTradeForm.builder()
                .title("제목")
                .context("내용")
                .price(12000)
                .category(Category.DIGITAL)
                .isShare(false)
                .isOffer(false).build();

        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "data123".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "data123".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "data123".getBytes());
        MockMultipartFile images4 = new MockMultipartFile("images", "image4.png", "image/png", "data123".getBytes());
        MockMultipartFile images5 = new MockMultipartFile("images", "image5.png", "image/png", "data123".getBytes());

        MockMultipartFile json = new MockMultipartFile("form", "dd", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(form).getBytes());

        //then
        mockMvc.perform(multipart("/account/{accountId}/trade",loginResult.getAccountId())
                .file(images1)
                .file(images2)
                .file(images3)
                .file(images4)
                .file(images5)
                .file(json)
        ).andDo(print());
    }

}