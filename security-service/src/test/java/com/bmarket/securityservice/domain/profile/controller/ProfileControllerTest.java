package com.bmarket.securityservice.domain.profile.controller;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.controller.requestForm.RequestProfileForm;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import static com.bmarket.securityservice.internal_api.RequestFrmApi.PUT_PROFILE_IMAGE_URL;
import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ProfileControllerTest {

    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    JwtService jwtService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;

    public MockWebServer mockWebServer = new MockWebServer();

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer.start(8095);
        MockResponse response = new MockResponse();
        response.setBody("default");
        mockWebServer.enqueue(response);
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
        accountCommandService.signUpProcessing(form);
    }

    @AfterEach
    void afterEach() throws IOException {
        accountRepository.deleteAll();
        mockWebServer.shutdown();
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    @DisplayName("프로필정보 조회 테스트")
    void getProfileTest() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("loginId", "login123!@#");
        Account account = accountRepository.findById(login.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, login.getAccessToken());
        headers.set(REFRESH_HEADER, login.getRefreshToken());
        headers.set(CLIENT_ID, login.getClientId());

        mockMvc.perform(get("/profile/" + login.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("SUCCESS"))
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("message").value("성공"))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.accountId").exists())
                .andExpect(jsonPath("result.nickname").value(account.getProfile().getNickname()))
                .andExpect(jsonPath("result.profileImage").value(account.getProfile().getProfileImage()))
                .andExpect(jsonPath("result.addressRange").value(account.getProfile().getAddressRange().name()))
                .andExpect(jsonPath("result.fullAddress").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 닉네임 수정 테스트")
    void putNickNameTest() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("loginId", "login123!@#");
        Account account = accountRepository.findById(login.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, login.getAccessToken());
        headers.set(REFRESH_HEADER, login.getRefreshToken());
        headers.set(CLIENT_ID, login.getClientId());

        RequestProfileForm.UpdateNickname newNickName = new RequestProfileForm.UpdateNickname("newNickName");
        String content = objectMapper.writeValueAsString(newNickName);
        //when
        mockMvc.perform(put("/profile/" + login.getAccountId() + "/nickname")
                        .content(content)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0]").exists())
                .andExpect(jsonPath("$.result[1]").exists())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("프로필 주소검색 범위 변경 테스트")
    void putAddressSearchRangeTest() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("loginId", "login123!@#");
        Account account = accountRepository.findById(login.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, login.getAccessToken());
        headers.set(REFRESH_HEADER, login.getRefreshToken());
        headers.set(CLIENT_ID, login.getClientId());

        //when
        mockMvc.perform(put("/profile/" + login.getAccountId() + "/range")
                        .param("range", AddressRange.TEN.name()))
                .andExpect(status().isOk())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("프로필 주소 변경 테스트")
    void putAddressTest() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("loginId", "login123!@#");
        Account account = accountRepository.findById(login.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, login.getAccessToken());
        headers.set(REFRESH_HEADER, login.getRefreshToken());
        headers.set(CLIENT_ID, login.getClientId());

        RequestProfileForm.UpdateAddress address = new RequestProfileForm.UpdateAddress(1003, "어디시", "어디구", "어디동");
        String content = objectMapper.writeValueAsString(address);
        //when
        mockMvc.perform(put("/profile/" + account.getId() + "/address")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
        //then

    }

    @Test
    @DisplayName("프로필 이미지 변경 테스트")
    void putImageTest() throws Exception {
        //given
        LoginResult login = jwtService.loginProcessing("loginId", "login123!@#");
        Account account = accountRepository.findById(login.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, login.getAccessToken());
        headers.set(REFRESH_HEADER, login.getRefreshToken());
        headers.set(CLIENT_ID, login.getClientId());

        MockMultipartFile file = new MockMultipartFile("image", "newImg.jpg", "jpg", "sfasfa".getBytes());

        mockWebServer.start(8095);
        mockWebServer.url(PUT_PROFILE_IMAGE_URL + login.getAccountId());
        MockResponse response = new MockResponse();
        response.setBody(file.getOriginalFilename());
        mockWebServer.enqueue(response);

        //when
        MockMultipartHttpServletRequestBuilder builder = multipart("/profile/" + account.getId() + "/image");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(file)
                        .headers(headers))
                .andExpect(status().isOk())
                .andDo(print());
        //then

    }
}