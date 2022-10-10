package com.bmarket.securityservice.service;

import com.bmarket.securityservice.entity.JwtCode;
import com.bmarket.securityservice.entity.RefreshToken;
import com.bmarket.securityservice.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService implements InitializingBean {

    private final UserDetailServiceImpl userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String SECRET_KEY = "MySecreteKeyIsMySecreteKeyItIsMustBeGreaterThen512bits";
    private final long TOKEN_VALIDATION_MS = 3600;
    private final long REFRESH_VALIDATION_MS = 36000;
    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(authentication.getName())//정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(validity) //만료시간 설정
                .signWith(key, SignatureAlgorithm.HS512)//암호화
                .compact();
    }

    private String generateRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String reissueRefreshToken(String refreshToken) throws RuntimeException {
        Authentication authentication = getAuthentication(refreshToken);

        RefreshToken token = refreshTokenRepository.findByClientId(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("REFRESH TOKEN NOT FOUN"));

        if (token.equals(refreshToken)) {
            String newRefreshToken = generateToken(authentication);
            token.changeRefreshToken(newRefreshToken);
            return newRefreshToken;

        } else {
            throw new IllegalArgumentException("NOT MATCH REFRESH TOKEN");
        }
    }

    public String issuedRefreshToken(Authentication authentication) {
        String newRefreshToken = generateRefreshToken(authentication);
        refreshTokenRepository.findByClientId(authentication.getName()).ifPresentOrElse(
                m -> m.changeRefreshToken(newRefreshToken),
                () -> {
                    RefreshToken refreshToken = RefreshToken.createRefreshToken(authentication.getName(), newRefreshToken);
                    refreshTokenRepository.save(refreshToken);
                });
        return newRefreshToken;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        return userDetailService.generateAuthentication(getUserPk(token).getSubject());
    }

    // 토큰에서 회원 정보 추출
    public Claims getUserPk(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 토큰의 유효성 + 만료일자 확인
    public JwtCode validateToken(String jwtToken) {

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
