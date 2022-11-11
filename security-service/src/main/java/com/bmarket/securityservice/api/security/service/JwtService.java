package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.security.entity.RefreshToken;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    public String issuedRefreshToken(String clientId) {
        log.info("[리프레쉬 토큰 발급]");

        Account account = getAccountByClientId(clientId);

        String newRefreshToken = jwtUtils.generateRefreshToken(clientId);

        if (account.getRefreshToken() != null) {
            account.getRefreshToken().changeRefreshToken(newRefreshToken);
        }

        RefreshToken refreshToken = RefreshToken.createRefreshToken(newRefreshToken);
        account.addRefresh(refreshToken);

        return newRefreshToken;
    }
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

    private Account getAccountByClientId(String clientId) {
        return accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
    }
}
