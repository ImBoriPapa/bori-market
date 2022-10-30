package com.bmarket.securityservice.utils.jwt;

import com.bmarket.securityservice.api.security.entity.JwtCode;
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

import static com.bmarket.securityservice.api.security.entity.JwtHeader.JWT_HEADER_PREFIX;

@Component
@Slf4j
public class JwtUtils implements InitializingBean {

    private final String SECRET_KEY;
    private final long TOKEN_VALIDATION_MS;
    private final long REFRESH_VALIDATION_MS;

    private Key key;
    public Optional<String> resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(JWT_HEADER_PREFIX)) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    public JwtUtils(@Value("${custom-key.secret-key}") String SECRET_KEY,
                    @Value("${custom-key.token-validation-life}")long TOKEN_VALIDATION_MS,
                    @Value("${custom-key.refresh-token-validation-life}") long REFRESH_VALIDATION_MS) {
        this.SECRET_KEY = SECRET_KEY;
        this.TOKEN_VALIDATION_MS = TOKEN_VALIDATION_MS*1000;
        this.REFRESH_VALIDATION_MS = REFRESH_VALIDATION_MS*1000;
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

    public String generateRefreshToken(String clientId) {
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

