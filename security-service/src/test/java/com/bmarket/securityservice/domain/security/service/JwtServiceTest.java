package com.bmarket.securityservice.domain.security.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.entity.RefreshToken;
import com.bmarket.securityservice.domain.security.repository.RefreshTokenRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FailLoginException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.InvalidTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.IsLogoutAccountException;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("local")
@SpringBootTest
@Slf4j
@Transactional
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RefreshTokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 테스트1 로그인 성공")
    void loginProcessingTest() throws Exception {
        //given
        String password = "token123";
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password(passwordEncoder.encode(password))
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        //when
        LoginResult loginResult = jwtService.loginProcessing(account.getLoginId(), password);

        //then
        assertThat(loginResult.getAccountId()).isEqualTo(savedAccount.getId());
        assertThat(loginResult.getRefreshToken()).isEqualTo("Bearer-" + savedAccount.getRefreshToken().getToken());
        assertThat(account.isLogin()).isTrue();
    }


    @Test
    @DisplayName("로그인 테스트2 로그인 실패 비밀번호가 다를 경우")
    void loginProcessingTest2() throws Exception {
        //given
        String password = "token123";
        String wrongPassword = "token1234";
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password(passwordEncoder.encode(password))
                .contact("0101313313")
                .build();
        //when
        Account savedAccount = accountRepository.save(account);
        //then
        assertThatThrownBy(() ->
                jwtService.loginProcessing(account.getLoginId(), wrongPassword)
        ).isExactlyInstanceOf(FailLoginException.class);
    }

    @Test
    @DisplayName("로그아웃 테스트1 로그아웃 성공")
    void logoutProcessing1() throws Exception {
        //given
        String password = "token123";
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password(passwordEncoder.encode(password))
                .contact("0101313313")
                .build();
        Account save = accountRepository.save(account);
        //when
        LoginResult loginResult = jwtService.loginProcessing(save.getLoginId(), password);
        jwtService.logoutProcessing(loginResult.getAccountId());
        Account findAccount = accountRepository.findById(save.getId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        //then
        assertThat(findAccount.isLogin()).isFalse();
        assertThat(findAccount.getRefreshToken().getToken()).isNull();

    }

    @Test
    @DisplayName("리프레시 토큰 발급 테스트 저장된 토큰이 없을 경우")
    void issuedRefreshTokenTest1() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);

        //when
        String issuedRefreshToken = jwtService.issuedRefreshToken(savedAccount);
        Account findAccount = accountRepository.findByClientId(savedAccount.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        RefreshToken findToken = tokenRepository.findById(findAccount.getRefreshToken().getId())
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을수 없습니다."));
        //then
        assertThat(findAccount.getRefreshToken().getToken()).isEqualTo(issuedRefreshToken);
        assertThat(findToken.getToken()).isEqualTo(findAccount.getRefreshToken().getToken());
    }

    @Test
    @DisplayName("리프레시 토큰 발급 테스트 저장된 토큰이 있을 경우")
    void issuedRefreshTokenTest2() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        String refreshToken = jwtUtils.generateRefreshToken(savedAccount.getId());
        RefreshToken token = RefreshToken.createRefreshToken(refreshToken);
        account.addRefresh(token);

        Thread.sleep(1000);

        //when
        String before = accountRepository.findByClientId(savedAccount.getClientId())
                .orElseThrow().getRefreshToken().getToken();

        String after = jwtService.issuedRefreshToken(savedAccount);

        //then
        assertThat(after).isNotEqualTo(before);

    }

    /**
     * 시나리오 1 리프레시 토큰에 저장된 clientId 로 계정을 찾을 수 없을 경우
     * ex : 잘못된 혹은 잘못 위조된 토큰
     *
     * @throws InvalidTokenException
     */
    @Test
    @DisplayName("리프레시 토큰 재발급 테스트 clientId로 계정을 찾을수 없을 경우")
    void reissueRefreshTokenTest1() throws Exception {
        //given
        Long wrongId = 314141L;
        String refreshToken = jwtUtils.generateRefreshToken(wrongId);
        //when

        //then
        assertThatThrownBy(() -> jwtService.reissueRefreshToken(refreshToken,314141L))
                .isExactlyInstanceOf(InvalidTokenException.class);
    }

    /**
     * 시나리오 2 토큰에 저장된 clientId로 계정을 찾고 계정에 저장된 리프레시 토큰이 없을 경우
     *
     * @throws Exception
     */
    @Test
    @DisplayName("리프레시 토큰 재발급 테스트 저장된 토큰이 없을 경우")
    void reissueRefreshTokenTest2() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        String refreshToken = jwtUtils.generateRefreshToken(savedAccount.getId());
        RefreshToken token = RefreshToken.createRefreshToken(refreshToken);
        savedAccount.addRefresh(token);
        savedAccount.getRefreshToken().deleteToken();
        //when

        //then
        assertThatThrownBy(() -> jwtService.reissueRefreshToken(refreshToken,savedAccount.getId()))
                .isExactlyInstanceOf(IsLogoutAccountException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 재발급 테스트 저장된 토큰이 있지만 토큰이 일치하지 않는 경우")
    void reissueRefreshTokenTest3() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        String refreshToken = jwtUtils.generateRefreshToken(account.getId());
        RefreshToken token = RefreshToken.createRefreshToken(refreshToken);
        savedAccount.addRefresh(token);
        Thread.sleep(1000);
        String notStoredToken = jwtUtils.generateRefreshToken(savedAccount.getId());
        //when
        log.info("savedToken= {}", refreshToken);
        log.info("notStoredToken= {}", notStoredToken);

        //then
        assertThatThrownBy(() -> jwtService.reissueRefreshToken(notStoredToken,savedAccount.getId()))
                .isExactlyInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("리프레시 토큰 정상 재발급")
    void reissueRefreshTokenTest4() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        String refreshToken = jwtUtils.generateRefreshToken(savedAccount.getId());
        RefreshToken token = RefreshToken.createRefreshToken(refreshToken);
        savedAccount.addRefresh(token);
        //when
        Thread.sleep(1000);
        String newRefresh = jwtService.reissueRefreshToken(refreshToken,savedAccount.getId());
        //then
        assertThat(newRefresh).isNotEqualTo(refreshToken);

    }
}