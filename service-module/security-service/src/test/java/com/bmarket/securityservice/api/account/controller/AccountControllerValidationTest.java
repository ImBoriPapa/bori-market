package com.bmarket.securityservice.api.account.controller;

import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@Transactional
@AutoConfigureMockMvc
class AccountControllerValidationTest {

    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        log.info("[TEST DATA INIT]");
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("testId")
                .name("tester")
                .nickname("test")
                .password("test123")
                .email("test@test.com")
                .contact("010-1122-1234")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        accountCommandService.signUpProcessing(createForm);
        log.info("[TEST DATA INIT FINISH]");
    }

    @Test
    @DisplayName("로그인 아이디 중복 검증 테스트")
    void checkDuplicateLoginId() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("testId")
                .name("tester2")
                .nickname("test")
                .password("test123")
                .email("test2@test.com")
                .contact("010-1000-1234")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        String requestValue = objectMapper.writeValueAsString(createForm);
        //when
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestValue))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("errorType").exists())
                .andExpect(jsonPath("errorCode").exists())
                .andExpect(jsonPath("message").exists())
                .andDo(print());

    }

}