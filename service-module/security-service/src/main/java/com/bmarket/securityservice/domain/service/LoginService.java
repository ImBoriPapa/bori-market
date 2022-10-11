package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.api.dto.LoginResult;
import com.bmarket.securityservice.domain.entity.Account;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Account account = accountRepository.findByLoginId(loginId).orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new BasicException(ErrorCode.FAIL_LOGIN);
        }

        Authentication authentication = userDetailService.generateAuthenticationByAccount(account);

        String token = "Bearer-"+jwtService.generateToken(authentication);
        String refreshToken = "Bearer-"+jwtService.issuedRefreshToken(authentication);

        return new LoginResult(account.getClientId(), token, refreshToken, account.getLastLoginTime());
    }
}