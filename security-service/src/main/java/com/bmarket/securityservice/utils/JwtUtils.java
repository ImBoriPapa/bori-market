package com.bmarket.securityservice.utils;

import com.bmarket.securityservice.security.constant.JwtCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static com.bmarket.securityservice.security.constant.SecurityHeader.JWT_HEADER_PREFIX;

@Component
@Slf4j
public class JwtUtils implements InitializingBean {

    private final String SECRET_KEY;
    private final long TOKEN_VALIDATION_MS;
    private final long REFRESH_VALIDATION_MS;
    private Key key;

    public JwtUtils(@Value("${custom-key.secret-key}") String SECRET_KEY,
                    @Value("${custom-key.token-validation-life}") long TOKEN_VALIDATION_MS,
                    @Value("${custom-key.refresh-token-validation-life}") long REFRESH_VALIDATION_MS) {
        this.SECRET_KEY = SECRET_KEY;
        this.TOKEN_VALIDATION_MS = TOKEN_VALIDATION_MS * 1000;
        this.REFRESH_VALIDATION_MS = REFRESH_VALIDATION_MS * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("[JwtUtils.afterPropertiesSet]");
        String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    /**
     * 토큰 생성
     * @param memberId
     * setSubject : 정보 저장
     * setIssuedAt() : 토큰 발행 시간 정보
     * signWith() : 암호화
     */
    public String generateAccessToken(String memberId) {
        log.info("[엑세스 토큰생성]");
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    //리프레시 토큰 생성
    public String generateRefreshToken(String memberId) {
        log.info("[리프레쉬 토큰생성]");
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_VALIDATION_MS);
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getMemberId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

    }


    // 토큰의 유효성 + 만료일자 확인
    public JwtCode validateToken(String token) {
        log.info("==============[JWT_UTILS] 토큰 검증  =============");
        try {
            getClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return JwtCode.DENIED;
        }
    }

    // 헤더에서 토큰 확인
    public Optional<String> resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(JWT_HEADER_PREFIX)) {
            return Optional.ofNullable(bearerToken.substring(7));
        }
        return Optional.empty();
    }


    public Date getExpired(String token) {
        Jws<Claims> claimsJws = getClaimsJws(token);
        return claimsJws.getBody().getExpiration();
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}

