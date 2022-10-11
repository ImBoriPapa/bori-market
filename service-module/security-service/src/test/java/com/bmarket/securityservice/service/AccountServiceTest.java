package com.bmarket.securityservice.service;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.domain.service.AccountService;
import com.bmarket.securityservice.api.dto.FindAccountResult;
import com.bmarket.securityservice.api.dto.SignupResult;
import com.bmarket.securityservice.domain.entity.Account;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.domain.repository.AccountRepository;
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
class AccountServiceTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach(){
        log.info("=======================beforeEach ====================");
        for(int i=0; i<100; i++){
            RequestSignUpForm form = new RequestSignUpForm(
                    "bot"+i,
                    "사람"+i,
                    "nickname"+i,
                    "dari123",
                    "bot"+i+"@bori.com",
                    "010-12"+i+"-9933");
            SignupResult signupResult = accountService.signUpProcessing(form);
        }
    }

    @AfterEach
    void afterEach(){
        log.info("=======================AFTER EACH====================");
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreate() throws Exception{
        //given
        RequestSignUpForm form = new RequestSignUpForm(
                "dari",
                "아무개",
                "daridari",
                "dari123",
                "dari@dari.com",
                "010-2222-9933");
        SignupResult signupResult = accountService.signUpProcessing(form);
        //when
        Account findAccountById = accountService.findAccountByAccountId(signupResult.getAccountId());
        FindAccountResult findAccountByClient = accountService.findAccountByClientId(signupResult.getClientId());
        //then
        assertThat(findAccountById.getId()).isEqualTo(signupResult.getAccountId());
        assertThat(findAccountByClient.getClientId()).isEqualTo(signupResult.getClientId());
        assertThat(findAccountByClient.getCreatedAt()).isNotNull();
        assertThat(findAccountByClient.getCreatedAt()).isEqualTo(signupResult.getCreatedAt());
    }

   @Test
   @DisplayName("단건 조회 by accountId")
   void searchOneByAccountId() throws Exception{
       //given
       RequestSignUpForm form = new RequestSignUpForm(
               "test1",
               "tester1",
               "1test",
               "test123",
               "test@test.com",
               "010-1111-1111");
       SignupResult result = accountService.signUpProcessing(form);
       //when
       Account findAccount = accountService.findAccountByAccountId(result.getAccountId());
       //then
       assertThat(findAccount.getId()).isEqualTo(result.getAccountId());

   }

   @Test
   @DisplayName("조회 실패 테스트")
   void failSearchOne() throws Exception{
       //given
       RequestSignUpForm form = new RequestSignUpForm(
               "test1",
               "tester1",
               "1test",
               "test123",
               "test@test.com",
               "010-1111-1111");
       SignupResult result = accountService.signUpProcessing(form);

       //when

       //then
       assertThatThrownBy(
               () -> accountService.findAccountByClientId("fsafaf")
       ).isInstanceOf(BasicException.class);
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void searchAllTest() throws Exception{
        //given
        PageRequest request = PageRequest.of(0, 20, Sort.Direction.DESC, "id");

        //when

        //then

    }

}