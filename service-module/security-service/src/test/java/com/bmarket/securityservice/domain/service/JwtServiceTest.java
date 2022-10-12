package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.domain.entity.JwtCode;

import lombok.extern.slf4j.Slf4j;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class JwtServiceTest {

    @Autowired JwtService jwtService;

    /**
     *  토큰 유효 시간 설정 5초
     * @throws Exception
     */
    @Test
    @DisplayName("토큰 유효시간 테스트")
    void validateTimeTest() throws Exception{
        //given
        String clientId = "abc1234def";
        String generateToken = jwtService.generateToken(clientId);
        //when
        for(int i=1; i<=5; i++){
            log.info("[COUNT]= {}",i);
            Thread.sleep(1000L);
        }
        JwtCode jwtCode = jwtService.validateToken(generateToken);
        //then
        assertThat(jwtCode).isEqualTo(JwtCode.EXPIRED);
    }

}