package com.bmarket.securityservice.utils.jwt;

import com.fasterxml.uuid.Generators;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;
    /**
     *   테스트
     *   알고리즘 : SignatureAlgorithm.HS512
     *   바디 = clientId
     *   token-validation-life: 10초
     *
     * @throws Exception
     */
    @Test
    @DisplayName("토큰 생성 테스트")
    void generateTokenTest() throws Exception {
        //given
        String clientId = Generators.timeBasedGenerator().generate().toString();
        Key key = setKey();
        //when
        String generatedToken = jwtUtils.generateAccessToken(clientId);


        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(generatedToken);

        String algorithm = claimsJws.getHeader().getAlgorithm();
        String subject = claimsJws.getBody().getSubject();
        Date issuedAt = claimsJws.getBody().getIssuedAt();
        Date expiration = claimsJws.getBody().getExpiration();
        //then
        assertThat(algorithm).isEqualTo(SignatureAlgorithm.HS512.name()); //알고리즘 확인
        assertThat(subject).isEqualTo(clientId);                          // 바디 확인
        assertThat(issuedAt.before(expiration)).isTrue();                 // 유효시간 확인
        assertThat(expiration.getTime() - issuedAt.getTime()).isEqualTo(10000L); assertThat(issuedAt.before(expiration)).isTrue();// 유효시간 확인 10초
    }

    public Key setKey(){
        String secretKey = "MySecreteKeyIsMySecreteKeyItIsMustBeGreaterThen512bits";
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }


    @Test
    @DisplayName("토큰 검증 테스트")
    void validateTokenTest() throws Exception{
        //given
        String clientId = "fafasf12313fgdgwas";
        String generatedToken = jwtUtils.generateAccessToken(clientId);
        String wrongKey = "fsdgㄴ9Gds0g8s0ㅎㄴㅇㅎ6ㅎㄴg-s9gsgsdg32423l5n3252jnl352fafa";
        String encode = Base64.getEncoder().encodeToString(wrongKey.getBytes());
        Key key = Keys.hmacShaKeyFor(encode.getBytes());
        //when
        JwtCode accessToken = jwtUtils.validateToken(generatedToken);
        for(int i=0;i<11; i++){
            Thread.sleep(1000);
            System.out.println(i+"second");
        }
        JwtCode expiredToken = jwtUtils.validateToken(generatedToken);

        JwtCode deniedToken = jwtUtils.validateToken(generatedToken);

        //then
        assertThat(accessToken).isEqualTo(JwtCode.ACCESS);
        assertThat(expiredToken).isEqualTo(JwtCode.EXPIRED);
        assertThatThrownBy(() ->
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(generatedToken)
        ).isInstanceOf(JwtException.class);
    }
}