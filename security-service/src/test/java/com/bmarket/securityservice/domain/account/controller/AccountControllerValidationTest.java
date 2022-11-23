package com.bmarket.securityservice.domain.account.controller;


import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class AccountControllerValidationTest {

    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestDataProvider testDataProvider;

    public MockWebServer mockWebServer;
    /**
     * 테스트용 일반 계정 데이터
     *                 loginId("tester")
     *                 name("테스터")
     *                 password(passwordEncoder.encode("!@tester1234"))
     *                 email("test@test.com")
     *                 contact("01011112222")
     *                 nickname("test")
     *                 addressCode(1100)
     *                 city("서울")
     *                 district("종로구")
     *                 town("암사동")
     */
    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/profile/default");

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody("http://localhost:8095/frm/profile/default.jpg");

        mockWebServer.enqueue(mockResponse);

        testDataProvider.initAccount();
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
        testDataProvider.clearAccount();
    }

    @Test
    @DisplayName("로그인 아이디 중복 검증 테스트")
    void validationTest1() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("tester")
                .name("중복자1")
                .nickname("dudu1")
                .password("!@duplicate123")
                .email("duplicate1@test.com")
                .contact("111111111112")
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
                .andExpect(jsonPath("status").value(ResponseStatus.DUPLICATE_LOGIN_ID.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_LOGIN_ID).getErrorType()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_LOGIN_ID.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_LOGIN_ID.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("연락처 중복 검증 테스트")
    void validationTest2() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate2")
                .name("중복자2")
                .nickname("dudu2")
                .password("!@duplicate123")
                .email("duplicate2@test.com")
                .contact("01011112222")
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
                .andExpect(jsonPath("status").value(ResponseStatus.DUPLICATE_CONTACT.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_CONTACT).getErrorType()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_CONTACT.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_CONTACT.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 검증 테스트")
    void validationTest3() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate3")
                .name("중복자3")
                .nickname("dudu3")
                .password("!@duplicate123")
                .email("test@test.com")
                .contact("111111111113")
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
                .andExpect(jsonPath("status").value(ResponseStatus.DUPLICATE_EMAIL.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_EMAIL).getErrorType()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_EMAIL.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_EMAIL.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 아이디 공백 검증 테스트")
    void validation4() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("")
                .name("중복자4")
                .nickname("dudu4")
                .password("!@duplicate123")
                .email("duplicate4@test.com")
                .contact("111111111114")
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
                .andExpect(jsonPath("status").value(ResponseStatus.FAIL_VALIDATION.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_EMAIL).getErrorType()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.FAIL_VALIDATION.getCode()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 아이디 정규식 검증 테스트 영문자 or 영문자+숫자가 아닐때")
    void validation5() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("한글은 안되")
                .name("중복자5")
                .nickname("dudu5")
                .password("!@duplicate123")
                .email("duplicate5@test.com")
                .contact("111111111115")
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
                .andExpect(jsonPath("status").value(ResponseStatus.FAIL_VALIDATION.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException().toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.FAIL_VALIDATION.getCode()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 아이디 정규식 검증 테스트 공백이 섞여있는 영문자")
    void validation6() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("dupl icat e2")
                .name("중복자6")
                .nickname("dudu6")
                .password("!@duplicate123")
                .email("duplicate6@test.com")
                .contact("111111111116")
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
                .andExpect(jsonPath("status").value(ResponseStatus.FAIL_VALIDATION.toString()))
                .andExpect(jsonPath("errorType").value(new FormValidationException().toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.FAIL_VALIDATION.getCode()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 이름 공백 size 테스트")
    void validation7() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate7")
                .name("")
                .nickname("dudu7")
                .password("!@duplicate123")
                .email("duplicate7@test.com")
                .contact("111111111117")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        //when
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestAccountForm.CreateForm>> validate = validator.validate(createForm);
        //then
        List<String> strings = validate.stream().map(m -> m.getMessage()).collect(Collectors.toList());
        assertThat(strings).containsExactly("이름은 반드시 입력해야 합니다.");
    }

    @Test
    @DisplayName("닉네임 검증 테스트")
    void validation8() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate8")
                .name("sdsGgd")
                .nickname("")
                .password("!@duplicate123")
                .email("duplicate8@test.com")
                .contact("111111111118")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        //when
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestAccountForm.CreateForm>> validate = validator.validate(createForm);
        List<String> list = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        //then
        assertThat(list).contains("닉네임은 반드시 입력해야 합니다", "닉네임은 2~10자리 이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void validation9() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate9")
                .name("중복자9")
                .nickname("dudu9")
                .password("!@duplicate123")
                .email("duplicate9@test.com")
                .contact("111111111119")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        //when
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestAccountForm.CreateForm>> validate = validator.validate(createForm);
        List<String> list = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        //then
        for (String s : list) {
            log.info("result={}", s);
        }

    }

    @Test
    @DisplayName("연락처 검증 테스트")
    void validation10() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("duplicate")
                .name("중복자10")
                .nickname("dudu2")
                .password("!@duplicate123")
                .email("duplicate10@test.com")
                .contact("090-11111-1111")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        //when
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestAccountForm.CreateForm>> validate = validator.validate(createForm);
        List<String> list = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        //then
        assertThat(list).containsExactly("연락처는 숫자만 입력하세요. 입력 예)01012341234");
    }
}