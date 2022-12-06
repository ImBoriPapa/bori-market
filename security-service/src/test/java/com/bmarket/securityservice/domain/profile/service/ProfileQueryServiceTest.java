package com.bmarket.securityservice.domain.profile.service;


import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;

import com.bmarket.securityservice.domain.profile.controller.ProfileResultForm;

import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import lombok.extern.slf4j.Slf4j;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;




import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("local")
@SpringBootTest
@Slf4j
class ProfileQueryServiceTest {

    @Autowired
    ProfileQueryService profileQueryService;
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TestDataProvider testDataProvider;

    @BeforeEach
    void beforeEach(){
        testDataProvider.initAccount();
    }

    @AfterEach
    void afterEach() {
        testDataProvider.clearAccount();
    }

    @Transactional(readOnly = true)
    @Test
    @DisplayName("프로필 단건 조회 테스트")
    void getProfileTest() throws Exception {
        //given
        Account account = accountRepository.findByLoginId("tester")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        //when
        ProfileResultForm.ProfileResult profile = profileQueryService.getProfile(account.getId());

        //then
        assertThat(profile.getNickname()).isEqualTo(account.getProfile().getNickname());
        assertThat(profile.getFullAddress()).isNotEmpty();
    }

}