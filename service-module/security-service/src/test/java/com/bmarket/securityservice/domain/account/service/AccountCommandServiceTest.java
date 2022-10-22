package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.dto.SignupResult;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class AccountCommandServiceTest {

    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    @DisplayName("계정 생성 테스트")
    void success_create() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("bread")
                .name("브레드")
                .nickname("브레드피트")
                .password("bread1234")
                .email("bread@bread.com")
                .contact("010-2222-1234")
                .addressCode(1100)
                .build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        log.info("====================after====================");
        //when
        Account account = accountRepository.findByClientId(signupResult.getClientId()).get();
        //then


    }

}