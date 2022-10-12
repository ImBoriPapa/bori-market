package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.domain.entity.Account;
import com.bmarket.securityservice.domain.entity.JwtCode;
import com.bmarket.securityservice.domain.entity.RefreshToken;
import com.bmarket.securityservice.domain.repository.AccountRepository;
import com.bmarket.securityservice.domain.repository.RefreshTokenRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import io.jsonwebtoken.*;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class JwtService implements InitializingBean {

    private final AccountRepository accountRepository;
    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String SECRET_KEY;
    private final long TOKEN_VALIDATION_MS;
    private final long REFRESH_VALIDATION_MS;
    private Key key;

    public JwtService(AccountRepository accountRepository, UserDetailServiceImpl userDetailService, RefreshTokenRepository refreshTokenRepository,
                      @Value("${custom-key.secret-key}") String SECRET_KEY,
                      @Value("${custom-key.token-validation-life}") long TOKEN_VALIDATION_MS,
                      @Value("${custom-key.refresh-token-validation-life}") long REFRESH_VALIDATION_MS) {
        this.accountRepository = accountRepository;
        this.userDetailService = userDetailService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.SECRET_KEY = SECRET_KEY;
        this.TOKEN_VALIDATION_MS = TOKEN_VALIDATION_MS * 1000;
        this.REFRESH_VALIDATION_MS = REFRESH_VALIDATION_MS * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String generateToken(String clientId) {
        log.info("==============[JWT_SERVICE] 토큰생성 =============");
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(clientId)//정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(validity) //만료시간 설정
                .signWith(key, SignatureAlgorithm.HS512)//암호화
                .compact();
    }

    private String generateRefreshToken(String clientId) {
        log.info("==============[JWT_SERVICE] 리프레쉬 토큰생성 =============");
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(clientId)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String reissueRefreshToken(String refreshToken) throws RuntimeException {
        log.info("==============[JWT_SERVICE] 리프레쉬 토큰 재발급 =============");
        Claims userPk = getUserPk(refreshToken);
        RefreshToken tokenInDb = refreshTokenRepository.findByClientId(userPk.getSubject())
                .orElseThrow(() -> new IllegalArgumentException("REFRESH TOKEN NOT FOUN"));

        if (tokenInDb.getRefreshToken().equals(refreshToken)) {
            String newRefreshToken = generateToken(userPk.getSubject());
            tokenInDb.changeRefreshToken(newRefreshToken);
            return newRefreshToken;

        } else {
            throw new IllegalArgumentException("NOT MATCH REFRESH TOKEN");
        }
    }

    public String issuedRefreshToken(String clientId) {
        log.info("==============[JWT_SERVICE] 리프레쉬 토큰 발급 =============");
        String newRefreshToken = generateRefreshToken(clientId);
        refreshTokenRepository.findByClientId(clientId).ifPresentOrElse(
                m -> m.changeRefreshToken(newRefreshToken),
                () -> {
                    RefreshToken refreshToken = RefreshToken.createRefreshToken(clientId, newRefreshToken);
                    refreshTokenRepository.save(refreshToken);
                });
        return newRefreshToken;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        log.info("==============[JWT_SERVICE] Authentication 생성 요청 =============");
        Optional<Account> account = accountRepository.findByClientId(getUserPk(token).getSubject());
        if (!account.get().isLogin()) {
            throw new BasicException(ErrorCode.THIS_ACCOUNT_IS_LOGOUT);
        }

        return userDetailService.generateAuthenticationByClientId(getUserPk(token).getSubject());
    }

    // 토큰에서 회원 정보 추출
    public Claims getUserPk(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 토큰의 유효성 + 만료일자 확인
    public JwtCode validateToken(String jwtToken) {
        log.info("==============[JWT_SERVICE] 토큰 검증  =============");
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return JwtCode.DENIED;
        }

    }

}
