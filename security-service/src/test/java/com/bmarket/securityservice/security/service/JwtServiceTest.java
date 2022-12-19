package com.bmarket.securityservice.security.service;

import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.AccountRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FailLoginException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.security.api.LoginResult;
import com.bmarket.securityservice.security.api.dto.ReIssueResult;
import com.bmarket.securityservice.security.constant.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
@Transactional
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String email = "test@test.com";
    private String password = "!@test1234";

    @BeforeEach
    void beforeEach() {
        log.info("[TEST DATA INIT]");
        Account account = Account.createAccount()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name("tester")
                .nickName("testTest")
                .contact("01011112222")
                .authority(Authority.USER)
                .build();
        accountRepository.save(account);
        log.info("[TEST DATA INIT FINISH]");
    }

    @AfterEach
    void afterEach() {
        log.info("[TEST DATA DELETE]");
        accountRepository.deleteAll();
    }


    @Test
    @DisplayName("토큰 재발급 테스트 정상 재발행")
    void reissueTokenSuccessTest() throws Exception {
        //given
        LoginResult result = jwtService.loginProcessing(email, password);
        String memberId = result.getMemberId();
        String refreshToken = result.getRefreshToken().replace("Bearer-", "");
        //when
        ReIssueResult reIssueResult = jwtService.reissueTokenProcessing(memberId, refreshToken);
        String storedToken = accountRepository.findByMemberId(reIssueResult.getMemberId()).orElseThrow(() -> new IllegalArgumentException(""))
                .getRefreshToken().getToken();
        //then
        assertThat(reIssueResult.getMemberId()).isEqualTo(memberId);
        assertThat(reIssueResult.getAccessToken()).isNotNull();
        assertThat(reIssueResult.getRefreshToken().replace("Bearer-", "")).isEqualTo(storedToken);
    }

    @Test
    @DisplayName("토큰 재발급 테스트 잘못된 memberId")
    void reissueTokenFailTest1() throws Exception {
        //given
        String memberId = "wrongId";
        String refreshToken = "wrongToken";
        //when
        jwtService.loginProcessing(email, password);
        //then
        assertThatThrownBy(() -> jwtService.reissueTokenProcessing(memberId, refreshToken))
                .isExactlyInstanceOf(NotFoundAccountException.class)
                .hasMessage(ResponseStatus.NOT_FOUND_ACCOUNT.getMessage());
    }

    @Test
    @DisplayName("토큰 재발급 테스트 디비에 저장된 토큰과 다른 경우")
    void reissueTokenFailTest2() throws Exception {
        //given
        String refreshToken = "wrongToken";
        //when
        LoginResult result = jwtService.loginProcessing(email, password);
        String memberId = result.getMemberId();
        //then
        assertThatThrownBy(() -> jwtService.reissueTokenProcessing(memberId, refreshToken))
                .isExactlyInstanceOf(FailLoginException.class)
                .hasMessage(ResponseStatus.REFRESH_TOKEN_NOT_MATCH_STORED.getMessage());
    }
}