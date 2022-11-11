package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.security.entity.RefreshToken;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.EmptyTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.InvalidTokenException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.IsLogoutAccountException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import com.bmarket.securityservice.utils.status.ResponseStatus;
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

        Account account = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));

        String newRefreshToken = jwtUtils.generateRefreshToken(clientId);
        return account.getRefreshToken() == null ? creatAndGetRefresh(account, newRefreshToken)
                : account.getRefreshToken().changeRefreshToken(newRefreshToken);
    }

    private  String creatAndGetRefresh(Account account, String newRefreshToken) {
        RefreshToken refreshToken = RefreshToken.createRefreshToken(newRefreshToken);
        account.addRefresh(refreshToken);
        return refreshToken.getToken();
    }

    /**
     *
     */
    public String reissueRefreshToken(String refreshToken) {
        log.info("[리프레쉬 토큰 재발급]");
        String clientId = jwtUtils.getUserClientId(refreshToken);
        // 1.토큰에 저장된 clientId로 계정을 찾을 수 없을 시 InvalidException
        Account account = getAccountByClientIdInToken(clientId);
        // 2.Account 에 저장된 Refresh 토큰이 없을시
        checkRefreshInAccount(account);

        return getRefreshIfEqStored(refreshToken, clientId, account);
    }

    private String getRefreshIfEqStored(String refreshToken, String clientId, Account account) {
        if (account.getRefreshToken().getToken().equals(refreshToken)) {
            String refresh = jwtUtils.generateRefreshToken(clientId);
            account.getRefreshToken().changeRefreshToken(refresh);
            return refresh;
        } else {
            throw new IllegalArgumentException("NOT MATCH REFRESH TOKEN");
        }
    }

    /**
     * 저장된 리프레시 토큰이 없다면 로그아웃으로 간주
     * @param account
     */
    private void checkRefreshInAccount(Account account) {
        if (account.getRefreshToken() == null) {
            throw new IsLogoutAccountException(ResponseStatus.THIS_ACCOUNT_IS_LOGOUT);
        }
    }

    /**
     * token 에 저장된 clientId 로 계정 조회시 찾을 수 없다면 잘못된 토큰으로 간주 InvalidTokenException 반환
     * @param clientId
     * @return
     */
    private Account getAccountByClientIdInToken(String clientId) {
        return accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new InvalidTokenException(ResponseStatus.INVALID_REFRESH_TOKEN));
    }
}
