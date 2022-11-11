package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.security.entity.RefreshToken;
import com.bmarket.securityservice.api.security.repository.RefreshTokenRepository;

import com.bmarket.securityservice.api.security.service.JwtService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.InvalidTokenException;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;

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

    @Test
    @DisplayName("리프레시 토큰 발급 테스트 저장된 토큰이 없을 경우")
    void issuedRefreshTokenTest1() throws Exception{
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);

        //when
        String issuedRefreshToken = jwtService.issuedRefreshToken(savedAccount.getClientId());
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
    void issuedRefreshTokenTest2() throws Exception{
        //given
        Account account = Account.createAccount()
                .loginId("tokenId")
                .name("reToken")
                .password("token123")
                .contact("0101313313")
                .build();
        Account savedAccount = accountRepository.save(account);
        String refreshToken = jwtUtils.generateRefreshToken(savedAccount.getClientId());
        RefreshToken token = RefreshToken.createRefreshToken(refreshToken);
        account.addRefresh(token);

            Thread.sleep(1000);

        //when
        String before = accountRepository.findByClientId(savedAccount.getClientId())
                .orElseThrow().getRefreshToken().getToken();

        String after = jwtService.issuedRefreshToken(savedAccount.getClientId());

        //then
        assertThat(after).isNotEqualTo(before);

    }

    /**
     * 시나리오 1 리프레시 토큰에 저장된 clientId 로 계정을 찾을 수 없을 경우
     * ex : 잘못된 혹은 잘못 위조된 토큰
     * @throws InvalidTokenException
     */
    @Test
    @DisplayName("리프레시 토큰 재발급 테스트")
    void reissueRefreshTokenTest1() throws Exception{
        //given
        String noExistAccountClientId = Generators.timeBasedGenerator().generate().toString();
        String refreshToken = jwtUtils.generateRefreshToken(noExistAccountClientId);
        //when

        //then
        assertThatThrownBy(() -> jwtService.reissueRefreshToken(refreshToken))
                .isExactlyInstanceOf(InvalidTokenException.class);
    }

    // TODO: 2022/11/11 테스트 진행
    @Test
    @DisplayName("")
    void reissueRefreshTokenTest2() throws Exception{
        //given

        //when

        //then

    }
}