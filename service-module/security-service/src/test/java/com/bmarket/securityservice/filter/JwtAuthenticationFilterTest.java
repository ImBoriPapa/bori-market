package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.dto.LoginResult;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountService;
import com.bmarket.securityservice.domain.jwt.service.JwtService;
import com.bmarket.securityservice.domain.security.service.LoginService;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.bmarket.securityservice.domain.jwt.JwtHeader.AUTHORIZATION_HEADER;
import static com.bmarket.securityservice.domain.jwt.JwtHeader.REFRESH_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Transactional
class JwtAuthenticationFilterTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoginService loginService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void before() {
        RequestSignUpForm signUpForm = RequestSignUpForm.builder()
                .loginId("happy")
                .name("happyHappy")
                .password("happy123")
                .email("happy@happy.com")
                .nickname("happyMan")
                .contact("010-2323-1341")
                .build();
        accountService.signUpProcessing(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("인증없이 접근 가능한 경로")
    void freeAccess() throws Exception {
        //given
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk());
        //when

        //then

    }

    @Test
    @DisplayName("인증 토큰이 없는 경우")
    void emptyAccessToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        mockMvc.perform(get("/jwt-test1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/empty-token?token=access"));
        //when

        //then

    }

    @Test
    @DisplayName("인증이 성공한 경우")
    void successAccessToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        mockMvc.perform(get("/jwt-test1")
                .header(AUTHORIZATION_HEADER, login.getToken())
                .header(REFRESH_HEADER, login.getRefreshToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        //when

        //then
    }

    @Test
    @DisplayName("인증이 실패한 경우")
    void failAccessToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        mockMvc.perform(get("/jwt-test1")
                .header(AUTHORIZATION_HEADER, login.getToken() + 12)
                .header(REFRESH_HEADER, login.getRefreshToken() + 31)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is3xxRedirection());
        //when

        //then
    }

    /**
     * 토큰 유효 시간 5초
     * 6초 대기후 요청
     * REFRESH_HEADER 없음
     *
     * @throws Exception
     */
    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 헤더가 없을 경우 ")
    void expiredAccessToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        for (int i = 1; i <= 6; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }

        mockMvc.perform(get("/jwt-test1")
                .header(AUTHORIZATION_HEADER, login.getToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is3xxRedirection());
        //when

        //then
    }

    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 헤더가 ACCESS 경우")
    void successRefreshToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        for (int i = 1; i <= 6; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }

        mockMvc.perform(get("/jwt-test1")
                        .header(AUTHORIZATION_HEADER, login.getToken())
                        .header(REFRESH_HEADER, login.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().exists(AUTHORIZATION_HEADER))
                .andExpect(header().exists(REFRESH_HEADER));
        //when

        //then
    }

    @Test
    @DisplayName("access 토큰이 만료 되었고 리프레쉬 토큰이 EXPIRED 경우")
    void expiredRefreshToken() throws Exception {
        //given
        LoginResult login = loginService.login("happy", "happy123");
        for (int i = 1; i <= 15; i++) {
            log.info("count={}", i);
            Thread.sleep(1000);
        }

        mockMvc.perform(get("/jwt-test1")
                        .header(AUTHORIZATION_HEADER, login.getToken())
                        .header(REFRESH_HEADER, login.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/expired-token"));

        mockMvc.perform(get("/exception/expired-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));



        //when

        //then
    }

    @Test
    @DisplayName("JWT 필터 로그아웃 기능 테스트")
    void filter() throws Exception {
        //given

        LoginResult result = loginService.login("happy", "happy123");

        //when

        mockMvc.perform(get("/jwt-test1")
                        .header(AUTHORIZATION_HEADER, result.getToken())
                        .header(REFRESH_HEADER, result.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        loginService.logout(result.getClientId());


        //then
        Assertions.assertThatThrownBy(
                        () -> jwtService.getAuthentication(result.getToken().substring(7)))
                .isInstanceOf(BasicException.class);

    }

}