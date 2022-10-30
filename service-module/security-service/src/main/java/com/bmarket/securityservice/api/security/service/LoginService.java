package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.security.controller.LoginResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    public LoginResult login(String loginId, String password) {
        log.info("==============[LoginService] 로그인 실행  =============");
        Account account = accountRepository.findByLoginId(loginId).orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new BasicException(ErrorCode.FAIL_LOGIN);
        }

        account.setLogin(true);
        String token = "Bearer-"+jwtService.generateToken(account.getClientId());
        String refreshToken = "Bearer-"+jwtService.issuedRefreshToken(account.getClientId());

        return new LoginResult(account.getClientId(), token, refreshToken, account.getLastLoginTime());
    }

    public void logout(String clientId){
        log.info("==============[LoginService] 로그아웃 실행  =============");
        Optional<Account> account = accountRepository.findByClientId(clientId);
        account.get().setLogin(false);
    }
}