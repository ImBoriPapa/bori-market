package com.bmarket.securityservice.api.account.controller;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
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


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
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
                .contact("01011221234")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        accountCommandService.signUpProcessing(createForm);
        log.info("[TEST DATA INIT FINISH]");
    }

    @Test
    @DisplayName("로그인 아이디 중복 검증 테스트")
    void validationTest1() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("testId")
                .name("tester2")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("test2@test.com")
                .contact("01064351234")
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
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_LOGIN_ID).toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_LOGIN_ID.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_LOGIN_ID.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("연락처 중복 검증 테스트")
    void validationTest2() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("testId2")
                .name("tester2")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("test2@test.com")
                .contact("01011221234")
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
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_CONTACT).toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_CONTACT.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_CONTACT.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 검증 테스트")
    void validationTest3() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("testId2")
                .name("tester2")
                .password("123456qweer!@#$")
                .password("test123")
                .email("test@test.com")
                .contact("01053351234")
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
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_EMAIL).toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.DUPLICATE_EMAIL.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.DUPLICATE_EMAIL.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 아이디 공백 검증 테스트")
    void validation4() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("")
                .name("tester2")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
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
                .andExpect(jsonPath("errorType").value(new FormValidationException(ResponseStatus.DUPLICATE_EMAIL).toString()))
                .andExpect(jsonPath("errorCode").value(ResponseStatus.FAIL_VALIDATION.getCode()))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 아이디 정규식 검증 테스트 영문자 or 영문자+숫자가 아닐때")
    void validation5() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("한글@#%#@")
                .name("tester2")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
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
    void validation6() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("Empt y")
                .name("tester2")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
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
    void validation7() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("tester")
                .name("")
                .nickname("test")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
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
    void validation8() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("tester")
                .name("fsafs")
                .nickname("")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        //when
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestAccountForm.CreateForm>> validate = validator.validate(createForm);
        List<String> list = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        //then
        assertThat(list).containsExactly("닉네임은 반드시 입력해야 합니다", "닉네임은 2~10자리 이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void validation9() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("tester")
                .name("fsafs")
                .nickname("nickname")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("01053351234")
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
            log.info("result={}",s);
        }

    }


    @Test
    @DisplayName("연락처 검증 테스트")
    void validation10() throws Exception{
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .loginId("tester")
                .name("fsafs")
                .nickname("nickname")
                .password("123456qweer!@#$")
                .email("testd@test.com")
                .contact("010-5335-1234")
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