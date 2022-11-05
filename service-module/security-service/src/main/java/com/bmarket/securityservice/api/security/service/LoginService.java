package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.security.controller.LoginResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
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
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    public LoginResult loginProcessing(String loginId, String password) {
        log.info("[1.LoginService] 로그인 실행");
        log.info("[2.LOGIN ID 확인]");
        Account account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));
        log.info("[3.PASSWORD 확인]");
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new BasicException(ErrorCode.FAIL_LOGIN);
        }
        
        log.info("[4.LOGIN CHECK 확인]");
        account.loginCheck(true);
        
        log.info("[5.TOKEN 생성]");
        String token = "Bearer-"+jwtUtils.generateToken(account.getClientId());
        log.info("[6.REFRESH TOKEN 생성]");
        String refreshToken = "Bearer-"+jwtService.issuedRefreshToken(account.getClientId());
        log.info("[7.LOGIN PROCESSING 종료]");
        return new LoginResult(account.getClientId(), token, refreshToken, account.getLastLoginTime());
    }

    public void logout(String clientId){
        log.info("==============[LoginService] 로그아웃 실행  =============");
        Optional<Account> account = accountRepository.findByClientId(clientId);
        account.get().loginCheck(false);
    }
}