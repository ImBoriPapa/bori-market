package com.bmarket.securityservice.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.service.AccountQueryService;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class AccountQueryServiceTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountQueryService accountQueryService;

    @BeforeEach
    void beforeEach() {
        log.info("=======================beforeEach ====================");
        for (int i = 0; i < 100; i++) {
            RequestSignUpForm form = RequestSignUpForm.builder()
                    .loginId("사용자" + i + "아이디")
                    .name("사용자" + i)
                    .nickname("사용자" + i + "닉네임")
                    .password("사용자123")
                    .email("사용자" + i + "@사용자.com")
                    .contact("010-1111-" + i + "1")
                    .addressCode(1002).build();

        }
    }

    @AfterEach
    void afterEach() {
        log.info("=======================AFTER EACH====================");
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreate() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("사용자아이디")
                .name("사용자")
                .nickname("사용자닉네임")
                .password("사용자123")
                .email("사용자@사용자.com")
                .contact("010-1111-1111")
                .addressCode(1002).build();


        //when


    }

    @Test
    @DisplayName("단건 조회 by accountId")
    void searchOneByAccountId() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("사용자아이디")
                .name("사용자")
                .nickname("사용자닉네임")
                .password("사용자123")
                .email("사용자@사용자.com")
                .contact("010-1111-1111")
                .addressCode(1002).build();

        //when

        //then


    }

    @Test
    @DisplayName("조회 실패 테스트")
    void failSearchOne() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("사용자아이디")
                .name("사용자")
                .nickname("사용자닉네임")
                .password("사용자123")
                .email("사용자@사용자.com")
                .contact("010-1111-1111")
                .addressCode(1002).build();


        //when

        //then
        assertThatThrownBy(
                () -> accountQueryService.findAccountByClientId("fsafaf")
        ).isInstanceOf(BasicException.class);
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void searchAllTest() throws Exception {
        //given
        PageRequest request = PageRequest.of(0, 20, Sort.Direction.DESC, "id");

        //when

        //then

    }

}