package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.security.controller.requestForm.RequestLoginForm;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;


import static com.bmarket.securityservice.utils.status.AuthenticationFilterStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;

    // TODO: 2022/11/14 테스트 보강
    @Test
    @DisplayName("인증없이 접근 가능한 경로 POST:/login")
    void freeAccess() throws Exception {
        //given
        RequestLoginForm form = new RequestLoginForm("manager", "0000");
        String value = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/login")
                        .content(value).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print());
        //when

        //then
    }

    @Test
    @DisplayName("인증 필요한 접근에 토큰 및 클라이언트 아이디 둘다 없는 경우")
    void emptyTokenAndIdTest() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");

        mockMvc.perform(get("/account/" + result.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(EMPTY_BOTH.getUrl())).andDo(print());
        //when

        //then

    }


    @Test
    @DisplayName("인증 토큰이 없는 경우 GET:/account/{clientId}")
    void emptyAccessToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId() + 121314);

        mockMvc.perform(get("/account/" + result.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TOKEN_IS_EMPTY.getUrl())).andDo(print());
        //when

        //then

    }

    @Test
    @DisplayName("clientId가 잘못된 경우 경우 GET:/account/{clientId}")
    void wrongAccessToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId() + 121314);
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken());
        headers.add(REFRESH_HEADER, result.getRefreshToken());
        //when
        mockMvc.perform(get("/account/" + result.getAccountId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(CLIENT_ID_IS_INVALID.getUrl())).andDo(print());

        //then

    }

    @Test
    @DisplayName("인증이 성공한 경우 GET:/account/{clientId}")
    void successAccessToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId());
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken());
        headers.add(REFRESH_HEADER, result.getRefreshToken());
        //when
        mockMvc.perform(get("/account/" + result.getAccountId())
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(print());

        //then
    }

    @Test
    @DisplayName(" access token 인증이 실패한 경우 GET:/account/{clientId}")
    void failAccessToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId());
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken() + 3131);
        headers.add(REFRESH_HEADER, result.getRefreshToken());
        //when
        mockMvc.perform(get("/account/" + result.getAccountId())
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is3xxRedirection()).andDo(print());

        //then
    }

    /**
     * 토큰 유효 시간 5초
     * 6초 대기후 요청
     * REFRESH_HEADER 없음
     */
    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 헤더가 없을 경우 GET:/account/{clientId}")
    void expiredAccessToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        for (int i = 1; i <= 10; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId());
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken());

        mockMvc.perform(get("/account/" + result.getAccountId())
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is3xxRedirection()).andDo(print());
        //when

        //then
    }

    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 헤더가 정상인 경우 GET:/account/{clientId}")
    void successRefreshToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        for (int i = 1; i <= 10; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId());
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken());
        headers.add(REFRESH_HEADER, result.getRefreshToken());

        mockMvc.perform(get("/account/" + result.getAccountId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().exists(CLIENT_ID))
                .andExpect(header().exists(AUTHORIZATION_HEADER))
                .andExpect(header().exists(REFRESH_HEADER)).andDo(print());
        //when

        //then
    }

    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 토큰이 EXPIRED 경우 GET:/account/{clientId}")
    void expiredRefreshToken() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing("manager", "0000");
        for (int i = 1; i <= 16; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, result.getClientId());
        headers.add(AUTHORIZATION_HEADER, result.getAccessToken());
        headers.add(REFRESH_HEADER, result.getRefreshToken());

        mockMvc.perform(get("/account/" + result.getAccountId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(REFRESH_TOKEN_IS_EXPIRED.getUrl())).andDo(print());
        //when

        //then
    }


}