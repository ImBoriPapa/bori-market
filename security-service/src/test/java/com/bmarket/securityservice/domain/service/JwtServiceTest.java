package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.utils.jwt.JwtCode;

import com.bmarket.securityservice.api.security.service.JwtService;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    JwtUtils jwtUtils;

    @Test
    @DisplayName("리프레쉬 토큰 발급 테스트")
    void issuedRefreshTokenTest() throws Exception{
        //given
        
        //when
    
        //then
    
    }
}