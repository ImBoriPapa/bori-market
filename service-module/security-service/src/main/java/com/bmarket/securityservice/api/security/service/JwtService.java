package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.security.entity.RefreshToken;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.security.repository.RefreshTokenRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import io.jsonwebtoken.*;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final AccountRepository accountRepository;
    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public String reissueRefreshToken(String refreshToken) throws RuntimeException {
        log.info("==============[JWT_SERVICE] 리프레쉬 토큰 재발급 =============");
        String clientId = jwtUtils.getUserPk(refreshToken).getSubject();

        Account account = getAccountByClientId(clientId);

        if (account.getRefreshToken() != null) {
            if (account.getRefreshToken().getRefreshToken().equals(refreshToken)) {
                String generateRefreshToken = jwtUtils.generateRefreshToken(clientId);
                account.getRefreshToken().changeRefreshToken(generateRefreshToken);
                return account.getRefreshToken().getRefreshToken();
            }
        } else {
            throw new IllegalArgumentException("NOT MATCH REFRESH TOKEN");
        }
        return refreshToken;
    }



    public String issuedRefreshToken(String clientId) {
        log.info("==============[JWT_SERVICE] 리프레쉬 토큰 발급 =============");

        Account account = getAccountByClientId(clientId);

        String newRefreshToken = jwtUtils.generateRefreshToken(clientId);

        if (account.getRefreshToken() != null) {
            account.getRefreshToken().changeRefreshToken(newRefreshToken);
        }

        RefreshToken refreshToken = RefreshToken.createRefreshToken(newRefreshToken);
        account.addRefresh(refreshToken);

        return newRefreshToken;
    }

    private Account getAccountByClientId(String clientId) {
        Account account = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        return account;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        log.info("==============[JWT_SERVICE] Authentication 생성 요청 =============");
        Optional<Account> account = accountRepository.findByClientId(jwtUtils.getUserPk(token).getSubject());
        if (!account.get().isLogin()) {
            throw new BasicException(ErrorCode.THIS_ACCOUNT_IS_LOGOUT);
        }
        return userDetailService.generateAuthenticationByClientId(jwtUtils.getUserPk(token).getSubject());
    }


}
