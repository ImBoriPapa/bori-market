package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class AccountCommandServiceTest {
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreateTest() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("tester")
                .name("이테스트")
                .nickname("브레드피트")
                .password("bread1234")
                .email("bread@bread.com")
                .contact("010-2222-1234")
                .addressCode(1100)
                .city("서울")
                .district("종로구")
                .town("암사동")
                .build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        //when
        Account account = accountRepository.findByClientId(signupResult.getClientId()).get();

        //then
        assertThat(signupResult.getClientId()).isEqualTo(account.getClientId());
        assertThat(signupResult.getCreatedAt()).isEqualTo(account.getCreatedAt());
        assertThat(account.getId()).isNotNull();
        assertThat(account.getLoginId()).isEqualTo(form.getLoginId());
        assertThat(account.getName()).isEqualTo(form.getName());
        assertThat(passwordEncoder.matches(form.getPassword(), account.getPassword())).isTrue();
        assertThat(account.getAuthority()).isEqualTo(Authority.ROLL_USER);
        assertThat(account.getUpdatedAt()).isEqualTo(account.getCreatedAt());

        assertThat(account.getProfile().getNickname()).isEqualTo(form.getNickname());


    }

}