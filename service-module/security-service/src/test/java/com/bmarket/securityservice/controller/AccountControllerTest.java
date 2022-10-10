package com.bmarket.securityservice.controller;

import com.bmarket.securityservice.controller.external_spec.requestForm.RequestSignUpForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @DisplayName("계정 생성 테스트")
    void signUpTest() throws Exception {

        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .name("로그인")
                .password("login123")
                .email("login@login.com")
                .contact("010-2232-1313")
                .build();
        String value = objectMapper.writeValueAsString(form);

        //given
        mockMvc.perform(post("/account")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("code").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("result").isNotEmpty());

        //when

        //then

    }

}