package com.bmarket.securityservice.account.domain.service;

import com.bmarket.securityservice.account.api.RequestAccountForm;
import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Slf4j
class AccountCommandServiceTest {

    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("계정 생성 테스트")
    void createAccount() throws Exception{
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .email("test@test.com")
                .password("!@#$test1234")
                .name("개발자")
                .contact("010-1234-1234")
                .addressCode(1001)
                .city("도시명")
                .district("지역구")
                .town("동네")
                .build();
        //when
        Account account = accountCommandService.signUpProcessing(form);
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(()-> new IllegalArgumentException());
        //then
        assertThat(findAccount.getId()).isEqualTo(account.getId());

    }
}